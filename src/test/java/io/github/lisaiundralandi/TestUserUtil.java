package io.github.lisaiundralandi;

import io.github.lisaiundralandi.user.LoginRequest;
import io.github.lisaiundralandi.user.UserRepository;
import io.github.lisaiundralandi.user.UserRequest;
import io.github.lisaiundralandi.user.entity.User;
import io.github.lisaiundralandi.user.entity.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class TestUserUtil {

    public static final String DEFAULT_PASSWORD = "Password123!";

    @Autowired
    private Environment environment;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordUtil passwordUtil;

    public String getPort() {
        return environment.getProperty("local.server.port");
    }

    public void createUser(String login) {
        UserRequest userRequest = new UserRequest(login, DEFAULT_PASSWORD, DEFAULT_PASSWORD);
        restTemplate.postForEntity("http://localhost:" + getPort() + "/user", userRequest, Void.class);
    }

    public ResponseEntity<Void> loginUser(String login, String password) {
        return restTemplate.postForEntity("http://localhost:" + getPort() + "/login",
                new LoginRequest(login, password), Void.class);
    }

    public void createAndLogin(String login) {
        createUser(login);
        loginUser(login, DEFAULT_PASSWORD);
    }

    public void createAndLoginAdmin(String login) {
        byte[] result = passwordUtil.hash(DEFAULT_PASSWORD);

        userRepository.save(new User(login, result, UserType.ADMIN));
        loginUser(login, DEFAULT_PASSWORD);
    }
}
