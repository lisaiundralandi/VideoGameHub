package io.github.lisaiundralandi.user;

import org.springframework.http.HttpStatus;
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
        if (!userRequest.getPassword()
                .equals(userRequest.getPasswordConfirmations()) ||
                userRepository.doesLoginExist(userRequest.getLogin())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        userRepository.addUser(
                new User(userRequest.getLogin(), userRequest.getPassword()));
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
}
