package io.github.lisaiundralandi.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
public class UserController {
    private final UserRepository userRepository;
    private final CurrentLogin currentLogin;

    public UserController(UserRepository userRepository, CurrentLogin currentLogin) {
        this.userRepository = userRepository;
        this.currentLogin = currentLogin;
    }

    @PostMapping(path = "/user")
    public void createUser(@RequestBody @Valid UserRequest userRequest) {
        String password = userRequest.getPassword();

        if (!password.equals(userRequest.getPasswordConfirmations())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords do not match");
        } else if (userRepository.doesLoginExist(userRequest.getLogin())) {
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

        userRepository.addUser(
                new User(userRequest.getLogin(), password));
    }

    @PostMapping(path = "/login")
    public void login(@RequestBody LoginRequest loginRequest) {
        User user = userRepository.getUser(loginRequest.getLogin());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        String password = user.getPassword();
        String requestPassword = loginRequest.getPassword();

        if (!password.equals(requestPassword)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        currentLogin.setLogged(true);
        currentLogin.setLogin(loginRequest.getLogin());
    }

    @DeleteMapping(path = "/user")
    public void deleteUser() {
        if (!currentLogin.isLogged()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        userRepository.deleteUser(currentLogin.getLogin());
        currentLogin.setLogged(false);
        currentLogin.setLogin(null);
    }
}
