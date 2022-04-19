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

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
