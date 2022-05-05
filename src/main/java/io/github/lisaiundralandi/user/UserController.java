package io.github.lisaiundralandi.user;

import io.github.lisaiundralandi.LoginUtil;
import io.github.lisaiundralandi.PasswordUtil;
import io.github.lisaiundralandi.user.entity.User;
import io.github.lisaiundralandi.user.entity.UserType;
import io.github.lisaiundralandi.user.library.UserLibraryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Optional;

@RestController
public class UserController {
    private final UserRepository userRepository;
    private final CurrentLogin currentLogin;
    private final LoginUtil loginUtil;
    private final UserLibraryRepository userLibraryRepository;
    private final PasswordUtil passwordUtil;

    public UserController(UserRepository userRepository, CurrentLogin currentLogin,
                          LoginUtil loginUtil,
                          UserLibraryRepository userLibraryRepository,
                          PasswordUtil passwordUtil) {
        this.userRepository = userRepository;
        this.currentLogin = currentLogin;
        this.loginUtil = loginUtil;
        this.userLibraryRepository = userLibraryRepository;
        this.passwordUtil = passwordUtil;
    }

    @PostMapping(path = "/user")
    public void createUser(@RequestBody @Valid UserRequest userRequest) {
        String password = userRequest.getPassword();

        if (!password.equals(userRequest.getPasswordConfirmations())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords do not match");
        } else if (userRepository.existsById(userRequest.getLogin())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Login is already taken");
        }

        passwordUtil.checkPassword(userRequest.getPassword(), userRequest.getPasswordConfirmations());

        try {
            MessageDigest md = MessageDigest.getInstance("SHA3-256");
            byte[] result = md.digest(password.getBytes(StandardCharsets.UTF_8));

            userRepository.save(
                    new User(userRequest.getLogin(), result, UserType.STANDARD));
        } catch (NoSuchAlgorithmException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
        }

    }

    @PostMapping(path = "/login")
    public void login(@RequestBody LoginRequest loginRequest) {
        Optional<User> optionalUser = userRepository.findById(loginRequest.getLogin());
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        User user = optionalUser.get();
        byte[] password = user.getPassword();

        try {
            MessageDigest md = MessageDigest.getInstance("SHA3-256");
            byte[] result = md.digest(loginRequest.getPassword()
                    .getBytes(StandardCharsets.UTF_8));

            if (!Arrays.equals(password, result)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
            currentLogin.setLogged(true);
            currentLogin.setUser(user);

        } catch (NoSuchAlgorithmException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
        }

    }

    @DeleteMapping(path = "/user")
    @Transactional
    public void deleteUser() {
        loginUtil.checkIfLogged();

        try {
            userLibraryRepository.deleteAllByUserId(currentLogin.getLogin());
            userRepository.deleteById(currentLogin.getLogin());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            currentLogin.setLogged(false);
            currentLogin.setUser(null);
        }
    }

    @DeleteMapping(path = "/login")
    public void logout() {
        loginUtil.checkIfLogged();

        currentLogin.setLogged(false);
        currentLogin.setUser(null);
    }

    @PutMapping(path = "/user")
    public void changePassword(@RequestBody ChangePasswordRequest request) {
        loginUtil.checkIfLogged();

        Optional<User> optionalUser = userRepository.findById(currentLogin.getLogin());
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        User user = optionalUser.get();
        byte[] password = user.getPassword();

        try {
            MessageDigest md = MessageDigest.getInstance("SHA3-256");
            byte[] oldPassword = md.digest(request.getPassword()
                    .getBytes(StandardCharsets.UTF_8));

            if (!Arrays.equals(password, oldPassword)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }

            passwordUtil.checkPassword(request.getNewPassword(), request.getNewPasswordConfirmations());

            byte[] newPassword = md.digest(request.getNewPassword()
                    .getBytes(StandardCharsets.UTF_8));

            user.setPassword(newPassword);
            userRepository.save(user);

        } catch (NoSuchAlgorithmException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
