package io.github.lisaiundralandi.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    @Autowired
    private GameRepository gameRepository;

    ResponseEntity<JsonNode> response;
    String login;
    Long gameId = -1L;

    @After
    public void clean() {
        gameRepository.deleteAll();
    }

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

    @Kiedy("zaktualizuję grę")
    public void zaktualizuję_grę(io.cucumber.datatable.DataTable dataTable) {
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

        response = restTemplate.exchange("http://localhost:" + port + "/game/" + gameId,
                HttpMethod.PUT, new HttpEntity<>(gameRequest), JsonNode.class);
    }

    @Wtedy("gra zostanie zaktualizowana pomyślnie")
    public void gra_zostanie_zaktualizowana_pomyślnie() {
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Zakładającże("istnieją następujące gry")
    public void istnieją_następujące_gry(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> maps = dataTable.asMaps();

        for (var game : maps) {
            String[] ageRatings = game.get("ageRating")
                    .split(", ");

            gameRepository.save(Game.builder()
                    .title(game.get("title"))
                    .creator(game.get("creator"))
                    .publisher(game.get("publisher"))
                    .platform(game.get("platform"))
                    .yearOfPublishing(Integer.parseInt(game.get("yearOfPublishing")))
                    .ageRating(Arrays.asList(ageRatings))
                    .category(game.get("category"))
                    .description(game.get("description"))
                    .addedBy(game.get("addedBy"))
                    .build());
        }
    }

    @Kiedy("wyszukam gry pasujące do kryteriów")
    public void wyszukam_gry_pasujące_do_kryteriów(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> game = dataTable.asMap();

        String yearOfPublishingString = game.get("yearOfPublishing");
        int yearOfPublishing = 0;
        if (yearOfPublishingString != null) {
            yearOfPublishing = Integer.parseInt(yearOfPublishingString);
        }

        SearchQuery searchQuery = SearchQuery.builder()
                .title(game.get("title"))
                .creator(game.get("creator"))
                .publisher(game.get("publisher"))
                .platform(game.get("platform"))
                .yearOfPublishing(yearOfPublishing)
                .category(game.get("category"))
                .build();

        response = restTemplate.postForEntity("http://localhost:" + port + "/game/find", searchQuery, JsonNode.class);
    }

    @Wtedy("powinna zostać zwrócona lista gier")
    public void powinna_zostać_zwrócona_lista_gier(
            io.cucumber.datatable.DataTable dataTable) throws JsonProcessingException {
        List<Map<String, String>> maps = dataTable.asMaps();

        List<Game> expected = new ArrayList<>();

        for (var game : maps) {
            String[] ageRatings = game.get("ageRating")
                    .split(", ");

            expected.add(Game.builder()
                    .title(game.get("title"))
                    .creator(game.get("creator"))
                    .publisher(game.get("publisher"))
                    .platform(game.get("platform"))
                    .yearOfPublishing(Integer.parseInt(game.get("yearOfPublishing")))
                    .ageRating(Arrays.asList(ageRatings))
                    .category(game.get("category"))
                    .description(game.get("description"))
                    .addedBy(game.get("addedBy"))
                    .build());
        }

        JsonNode body = response.getBody();
        assertNotNull(body);

        assertEquals(expected.size(), body.size());

        for (int i = 0; i < body.size(); i++) {
            Game game = objectMapper.treeToValue(body.get(i), Game.class);

            Game expectedGame = expected.get(i);
            assertEquals(expectedGame.getTitle(), game.getTitle());
            assertEquals(expectedGame.getCreator(), game.getCreator());
            assertEquals(expectedGame.getPublisher(), game.getPublisher());
            assertEquals(expectedGame.getPlatform(), game.getPlatform());
            assertEquals(expectedGame.getYearOfPublishing(), game.getYearOfPublishing());
            assertEquals(expectedGame.getCategory(), game.getCategory());
            assertEquals(expectedGame.getAddedBy(), game.getAddedBy());
            assertEquals(expectedGame.getDescription(), game.getDescription());
            assertEquals(expectedGame.getAgeRating(), game.getAgeRating());
        }
    }

}
