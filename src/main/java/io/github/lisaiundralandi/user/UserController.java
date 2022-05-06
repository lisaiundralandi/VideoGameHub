package io.github.lisaiundralandi.user;

import io.github.lisaiundralandi.LoginUtil;
import io.github.lisaiundralandi.PasswordUtil;
import io.github.lisaiundralandi.user.entity.User;
import io.github.lisaiundralandi.user.entity.UserType;
import io.github.lisaiundralandi.user.library.UserLibraryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import javax.validation.Valid;
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
    @Operation(summary = "Dodaje użytkownika", responses = {
            @ApiResponse(responseCode = "200", description = "Użytkownik dodany pomyślnie"),
            @ApiResponse(responseCode = "400", description = "Użytkownik istnieje lub hasło nie spełnia wymagań")
    })
    public void createUser(@RequestBody @Valid UserRequest userRequest) {
        String password = userRequest.getPassword();

        if (!password.equals(userRequest.getPasswordConfirmations())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords do not match");
        } else if (userRepository.existsById(userRequest.getLogin())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Login is already taken");
        }

        passwordUtil.checkPassword(userRequest.getPassword(), userRequest.getPasswordConfirmations());

        byte[] result = passwordUtil.hash(password);

        userRepository.save(
                new User(userRequest.getLogin(), result, UserType.STANDARD));
    }

    @PostMapping(path = "/login")
    @Operation(summary = "Loguje użytkownika w bieżącej sesji", responses = {
            @ApiResponse(responseCode = "200", description = "Użytkownik zalogowany pomyślnie"),
            @ApiResponse(responseCode = "401", description = "Zły login lub hasło")
    })
    public void login(@RequestBody LoginRequest loginRequest) {
        User user = getUser(loginRequest.getLogin());
        byte[] password = user.getPassword();

        byte[] result = passwordUtil.hash(loginRequest.getPassword());

        if (!Arrays.equals(password, result)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        currentLogin.setLogged(true);
        currentLogin.setUser(user);
    }

    @DeleteMapping(path = "/user")
    @Transactional
    @Operation(summary = "Usuwa zalogowanego użytkownika", responses = {
            @ApiResponse(responseCode = "200", description = "Użytkownik usunięty pomyślnie"),
            @ApiResponse(responseCode = "401", description = "Użytkownik niezalogowany")
    })
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
    @Operation(summary = "Wylogowuje użytkownika", responses = {
            @ApiResponse(responseCode = "200", description = "Użytkownik wylogowany pomyślnie"),
            @ApiResponse(responseCode = "401", description = "Użytkownik niezalogowany")
    })
    public void logout() {
        loginUtil.checkIfLogged();

        currentLogin.setLogged(false);
        currentLogin.setUser(null);
    }

    @PutMapping(path = "/user")
    @Operation(summary = "Zmienia hasło użytkownika", responses = {
            @ApiResponse(responseCode = "200", description = "Hasło zmienione pomyślnie"),
            @ApiResponse(responseCode = "401", description = "Użytkownik niezalogowany"),
            @ApiResponse(responseCode = "400", description = "Hasło nie spełnia wymagań"),
            @ApiResponse(responseCode = "403", description = "Stare hasło jest niepoprawne")
    })
    public void changePassword(@RequestBody ChangePasswordRequest request) {
        loginUtil.checkIfLogged();

        User user = getUser(currentLogin.getLogin());
        byte[] password = user.getPassword();

        byte[] oldPassword = passwordUtil.hash(request.getPassword());

        if (!Arrays.equals(password, oldPassword)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        passwordUtil.checkPassword(request.getNewPassword(), request.getNewPasswordConfirmations());

        byte[] newPassword = passwordUtil.hash(request.getNewPassword());

        user.setPassword(newPassword);
        userRepository.save(user);
    }

    private User getUser(String login) {
        Optional<User> optionalUser = userRepository.findById(login);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return optionalUser.get();
    }
}
