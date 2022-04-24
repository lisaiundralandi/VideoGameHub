package io.github.lisaiundralandi.user;

import io.github.lisaiundralandi.LoginUtil;
import io.github.lisaiundralandi.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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

    public UserController(UserRepository userRepository, CurrentLogin currentLogin,
                          LoginUtil loginUtil) {
        this.userRepository = userRepository;
        this.currentLogin = currentLogin;
        this.loginUtil = loginUtil;
    }

    @PostMapping(path = "/user")
    public void createUser(@RequestBody @Valid UserRequest userRequest) {
        String password = userRequest.getPassword();

        if (!password.equals(userRequest.getPasswordConfirmations())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords do not match");
        } else if (userRepository.existsById(userRequest.getLogin())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Login is already taken");
        }

        boolean hasNoUpperCase = password.chars()
                .noneMatch(ch -> Character.isUpperCase(ch));
        boolean hasNoLowerCase = password.chars()
                .noneMatch(ch -> Character.isLowerCase(ch));
        boolean hasNoDigits = password.chars()
                .noneMatch(ch -> Character.isDigit(ch));
        boolean hasNoSpecialChars = password.chars()
                .allMatch(ch -> Character.isLetterOrDigit(ch));

        if (password.length() <= 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password too short");
        } else if (hasNoUpperCase) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password has no upper case characters");
        } else if (hasNoLowerCase) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password has no lower case characters");
        } else if (hasNoDigits) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password has no digits");
        } else if (hasNoSpecialChars) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password has no special characters");
        }

        try {
            MessageDigest md = MessageDigest.getInstance("SHA3-256");
            byte[] result = md.digest(password.getBytes(StandardCharsets.UTF_8));

            userRepository.save(
                    new User(userRequest.getLogin(), result));
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
            currentLogin.setLogin(loginRequest.getLogin());

        } catch (NoSuchAlgorithmException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
        }

    }

    @DeleteMapping(path = "/user")
    public void deleteUser() {
        loginUtil.checkIfLogged();

        try {
            userRepository.deleteById(currentLogin.getLogin());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            currentLogin.setLogged(false);
            currentLogin.setLogin(null);
        }
    }

    @DeleteMapping(path = "/login")
    public void logout() {
        loginUtil.checkIfLogged();

        currentLogin.setLogged(false);
        currentLogin.setLogin(null);
    }
}
