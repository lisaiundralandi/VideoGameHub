package io.github.lisaiundralandi.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.pl.Kiedy;
import io.cucumber.java.pl.Wtedy;
import io.cucumber.java.pl.Zakładającże;
import io.github.lisaiundralandi.ErrorResponse;
import io.github.lisaiundralandi.PasswordUtil;
import io.github.lisaiundralandi.game.entity.Game;
import io.github.lisaiundralandi.user.LoginRequest;
import io.github.lisaiundralandi.user.UserRepository;
import io.github.lisaiundralandi.user.entity.User;
import io.github.lisaiundralandi.user.entity.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings({"NonAsciiCharacters", "SpringJavaAutowiredMembersInspection"})
public class GameSteps {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PasswordUtil passwordUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    ResponseEntity<JsonNode> response;
    String login;
    Long gameId = -1L;

    @Zakładającże("użytkownik z loginem {string} istnieje i jest zalogowany")
    public void użytkownik_z_loginem_istnieje_i_jest_zalogowany(String login) {
        String password = "Password123!";
        byte[] result = passwordUtil.hash(password);
        userRepository.save(new User(login, result, UserType.STANDARD));

        LoginRequest loginRequest = new LoginRequest(login, password);
        restTemplate.postForEntity("http://localhost:" + port + "/login",
                loginRequest, Void.class);

        this.login = login;
    }

    @Kiedy("dodam grę")
    public void dodam_grę(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> game = dataTable.asMap();
        String[] ageRatings = game.get("ageRating")
                .split(", ");

        GameRequest gameRequest = GameRequest.builder()
                .title(game.get("title"))
                .creator(game.get("creator"))
                .publisher(game.get("publisher"))
                .platform(game.get("platform"))
                .yearOfPublishing(Integer.parseInt(game.get("yearOfPublishing")))
                .ageRating(Arrays.asList(ageRatings))
                .category(game.get("category"))
                .description(game.get("description"))
                .build();

        response = restTemplate.postForEntity("http://localhost:" + port + "/game",
                gameRequest, JsonNode.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode body = Objects.requireNonNull(response.getBody());
            gameId = body.asLong();
        }
    }

    @Wtedy("gra zostanie dodana pomyślnie")
    public void gra_zostanie_dodana_pomyślnie() {
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Wtedy("szczegóły gry to")
    public void szczegóły_gry_to(io.cucumber.datatable.DataTable dataTable) {
        ResponseEntity<Game> gameResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/game/" + gameId,
                Game.class);

        Map<String, String> game = dataTable.asMap();
        String[] ageRatings = game.get("ageRating")
                .split(", ");

        Game expected = Game.builder()
                .id(gameId)
                .title(game.get("title"))
                .creator(game.get("creator"))
                .publisher(game.get("publisher"))
                .platform(game.get("platform"))
                .yearOfPublishing(Integer.parseInt(game.get("yearOfPublishing")))
                .ageRating(Arrays.asList(ageRatings))
                .category(game.get("category"))
                .description(game.get("description"))
                .addedBy(login)
                .build();
        Game actual = gameResponse.getBody();
        assertEquals(expected, actual);
    }

    @Wtedy("powinien zostać zwrócony błąd {}")
    public void powinien_zostać_zwrócony_błąd(HttpStatus httpStatus) {
        assertEquals(httpStatus, response.getStatusCode());
    }

    @Wtedy("komunikat błędu {string}")
    public void komunikat_błędu(String message) throws JsonProcessingException {
        ErrorResponse errorResponse = objectMapper.treeToValue(response.getBody(), ErrorResponse.class);
        assertEquals(message, errorResponse.getMessage());
    }

    @Zakładającże("nie jestem zalogowana")
    public void nie_jestem_zalogowana() {
        restTemplate.delete("http://localhost:" + port + "/login");
    }

    @Zakładającże("administrator z loginem {string} istnieje i jest zalogowany")
    public void administrator_z_loginem_istnieje_i_jest_zalogowany(String login) {
        String password = "Password123!";
        byte[] result = passwordUtil.hash(password);
        userRepository.save(new User(login, result, UserType.ADMIN));

        LoginRequest loginRequest = new LoginRequest(login, password);
        restTemplate.postForEntity("http://localhost:" + port + "/login",
                loginRequest, Void.class);

        this.login = login;
    }

    @Kiedy("usunę grę")
    public void usunę_grę() {
        response = restTemplate.exchange("http://localhost:" + port + "/game/" + gameId,
                HttpMethod.DELETE, null, JsonNode.class);
    }

    @Wtedy("gra zostanie usunięta pomyślnie")
    public void gra_zostanie_usunięta_pomyślnie() {
        ResponseEntity<ErrorResponse> gameResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/game/" + gameId,
                ErrorResponse.class);

        assertEquals(HttpStatus.NOT_FOUND, gameResponse.getStatusCode());
    }
}
