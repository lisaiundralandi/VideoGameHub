package io.github.lisaiundralandi.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    private static final String DEFAULT_PASSWORD = "Password123!";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateUser() {
        UserRequest userRequest = new UserRequest("tester", "Password123!", "Password123!");
        ResponseEntity<Void> response = restTemplate.postForEntity("http://localhost:" + port + "/user",
                userRequest, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldLoginUser() {
        createUser("tester1");
        ResponseEntity<Void> response = loginUser("tester1", DEFAULT_PASSWORD);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldChangePassword() {
        createUser("tester2");
        loginUser("tester2", DEFAULT_PASSWORD);

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(DEFAULT_PASSWORD, "Password123!!",
                "Password123!!");
        restTemplate.put("http://localhost:" + port + "/user", changePasswordRequest);

        ResponseEntity<Void> response = loginUser("tester2", "Password123!!");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldNotLoginUser() {
        createUser("wrongPassword");
        ResponseEntity<Void> response = loginUser("wrongPassword", "Password23!");
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void shouldDeleteUser() {
        createUser("testerToDelete");
        loginUser("testerToDelete", DEFAULT_PASSWORD);

        restTemplate.delete("http://localhost:" + port + "/user");

        ResponseEntity<Void> response = loginUser("testerToDelete", DEFAULT_PASSWORD);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void shouldLogoutUser() {
        createUser("testerToLogout");
        loginUser("testerToLogout", DEFAULT_PASSWORD);

        restTemplate.delete("http://localhost:" + port + "/login");

        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:" + port + "/user", HttpMethod.DELETE,
                new HttpEntity<>((Void) null, null), void.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    private void createUser(String login) {
        UserRequest userRequest = new UserRequest(login, UserControllerTest.DEFAULT_PASSWORD,
                UserControllerTest.DEFAULT_PASSWORD);
        restTemplate.postForEntity("http://localhost:" + port + "/user", userRequest, Void.class);
    }

    private ResponseEntity<Void> loginUser(String login, String password) {
        return restTemplate.postForEntity("http://localhost:" + port + "/login",
                new LoginRequest(login, password), Void.class);
    }
}