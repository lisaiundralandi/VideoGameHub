package io.github.lisaiundralandi.user;

import io.cucumber.java.Before;
import io.cucumber.java.pl.Kiedy;
import io.cucumber.java.pl.Wtedy;
import io.cucumber.java.pl.Zakładającże;
import io.github.lisaiundralandi.ErrorResponse;
import io.github.lisaiundralandi.user.entity.User;
import io.github.lisaiundralandi.user.entity.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserSteps {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    private ResponseEntity<ErrorResponse> response;

    @Before
    public void clean() {
        userRepository.deleteAll();
    }

    @Kiedy("podam login {string} i hasło {string} i potwierdzenie hasła {string}")
    public void podam_login_i_hasło_i_potwierdzenie_hasła(String login, String password, String passwordConfirmation) {
        UserRequest userRequest = new UserRequest(login, password, passwordConfirmation);
        response = restTemplate.postForEntity("http://localhost:" + port + "/user",
                userRequest, ErrorResponse.class);
    }

    @Wtedy("użytkownik powinien zostać poprawnie zarejestrowany")
    public void użytkownik_powinien_zostać_poprawnie_zarejestrowany() {
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Wtedy("powinien zostać zwrócony błąd {}")
    public void powinien_zostać_zwrócony_błąd(HttpStatus httpStatus) {
        assertEquals(httpStatus, response.getStatusCode());
    }

    @Wtedy("komunikat błędu {string}")
    public void komunikat_błędu(String message) {
        ErrorResponse responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(message, responseBody.getMessage());
    }

    @Zakładającże("użytkownik z loginem {string} już istnieje")
    public void użytkownik_z_loginem_już_istnieje(String login) {
        userRepository.save(new User(login, new byte[]{}, UserType.STANDARD));
    }

}
