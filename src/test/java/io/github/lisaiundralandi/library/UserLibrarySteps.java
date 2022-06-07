package io.github.lisaiundralandi.library;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.pl.Kiedy;
import io.cucumber.java.pl.Wtedy;
import io.cucumber.java.pl.Zakładającże;
import io.github.lisaiundralandi.ErrorResponse;
import io.github.lisaiundralandi.PasswordUtil;
import io.github.lisaiundralandi.game.GameRepository;
import io.github.lisaiundralandi.game.entity.Game;
import io.github.lisaiundralandi.user.LoginRequest;
import io.github.lisaiundralandi.user.UserRepository;
import io.github.lisaiundralandi.user.entity.User;
import io.github.lisaiundralandi.user.entity.UserType;
import io.github.lisaiundralandi.user.library.AddGameToLibraryRequest;
import io.github.lisaiundralandi.user.library.UserLibraryRepository;
import io.github.lisaiundralandi.user.library.entity.GameInLibrary;
import io.github.lisaiundralandi.user.library.entity.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings({"SpringJavaAutowiredMembersInspection", "NonAsciiCharacters"})
public class UserLibrarySteps {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PasswordUtil passwordUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserLibraryRepository userLibraryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    Long gameId = -1L;
    ResponseEntity<JsonNode> response;
    String login;

    @After
    public void clean() {
        userLibraryRepository.deleteAll();
        gameRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Zakładającże("jestem zalogowana jako użytkownik z loginem {string}")
    public void jestem_zalogowana_jako_użytkownik_z_loginem(String login) {
        String password = "Password123!";
        byte[] result = passwordUtil.hash(password);
        userRepository.save(new User(login, result, UserType.STANDARD));
        this.login = login;

        LoginRequest loginRequest = new LoginRequest(login, password);
        restTemplate.postForEntity("http://localhost:" + port + "/login",
                loginRequest, Void.class);
    }

    @Zakładającże("istnieje następująca gra")
    public void istnieje_następująca_gra(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> gameParams = dataTable.asMap();
        String[] ageRatings = gameParams.get("ageRating")
                .split(", ");

        Game game = Game.builder()
                .title(gameParams.get("title"))
                .creator(gameParams.get("creator"))
                .publisher(gameParams.get("publisher"))
                .platform(gameParams.get("platform"))
                .yearOfPublishing(Integer.parseInt(gameParams.get("yearOfPublishing")))
                .ageRating(Arrays.asList(ageRatings))
                .category(gameParams.get("category"))
                .description(gameParams.get("description"))
                .build();

        gameId = gameRepository.save(game)
                .getId();
    }

    @Kiedy("dodam grę do biblioteki")
    public void dodam_grę_do_biblioteki() {
        AddGameToLibraryRequest addGameToLibraryRequest =
                new AddGameToLibraryRequest(gameId, 9.0, Status.OWNED, true);

        response = restTemplate.postForEntity("http://localhost:" + port + "/library",
                addGameToLibraryRequest, JsonNode.class);
    }

    @Wtedy("gra zostanie pomyślnie dodana do biblioteki")
    public void gra_zostanie_pomyślnie_dodana_do_biblioteki() {
        assertEquals(HttpStatus.OK, response.getStatusCode());
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

    @Zakładającże("w bibliotece użytkownika są następujące gry")
    public void w_bibliotece_użytkownika_są_następujące_gry(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> maps = dataTable.asMaps();

        for (var map : maps) {
            String[] ageRatings = map.get("ageRating")
                    .split(", ");

            Game game = gameRepository.save(Game.builder()
                    .title(map.get("title"))
                    .creator(map.get("creator"))
                    .publisher(map.get("publisher"))
                    .platform(map.get("platform"))
                    .yearOfPublishing(Integer.parseInt(map.get("yearOfPublishing")))
                    .ageRating(Arrays.asList(ageRatings))
                    .category(map.get("category"))
                    .description(map.get("description"))
                    .addedBy(map.get("addedBy"))
                    .build());

            String ratingString = map.get("rating");
            Double rating = null;
            if (ratingString != null) {
                rating = Double.parseDouble(ratingString);
            }

            String statusString = map.get("status");
            Status status = null;
            if (statusString != null) {
                status = Status.valueOf(statusString);
            }

            boolean played = Boolean.parseBoolean(map.get("played"));

            userLibraryRepository.save(new GameInLibrary(
                    login, null, game.getId(), game, rating, status, played
            ));
        }
    }

    @Kiedy("pobiorę listę gier z biblioteki")
    public void pobiorę_listę_gier_z_biblioteki() {
        response = restTemplate.getForEntity(
                "http://localhost:" + port + "/library", JsonNode.class);
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
            GameInLibrary gameInLibrary = objectMapper.treeToValue(body.get(i), GameInLibrary.class);

            Game expectedGame = expected.get(i);

            assertEquals(expectedGame.getTitle(), gameInLibrary.getGame()
                    .getTitle());
            assertEquals(expectedGame.getCreator(), gameInLibrary.getGame()
                    .getCreator());
            assertEquals(expectedGame.getPublisher(), gameInLibrary.getGame()
                    .getPublisher());
            assertEquals(expectedGame.getPlatform(), gameInLibrary.getGame()
                    .getPlatform());
            assertEquals(expectedGame.getYearOfPublishing(), gameInLibrary.getGame()
                    .getYearOfPublishing());
            assertEquals(expectedGame.getCategory(), gameInLibrary.getGame()
                    .getCategory());
            assertEquals(expectedGame.getAddedBy(), gameInLibrary.getGame()
                    .getAddedBy());
            assertEquals(expectedGame.getDescription(), gameInLibrary.getGame()
                    .getDescription());
            assertEquals(expectedGame.getAgeRating(), gameInLibrary.getGame()
                    .getAgeRating());
        }
    }
}
