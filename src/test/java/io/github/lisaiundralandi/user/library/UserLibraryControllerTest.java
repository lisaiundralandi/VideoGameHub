package io.github.lisaiundralandi.user.library;

import io.github.lisaiundralandi.TestUserUtil;
import io.github.lisaiundralandi.game.GameRepository;
import io.github.lisaiundralandi.game.entity.Game;
import io.github.lisaiundralandi.user.library.entity.GameInLibrary;
import io.github.lisaiundralandi.user.library.entity.GameInLibraryId;
import io.github.lisaiundralandi.user.library.entity.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserLibraryControllerTest {
    Game game;
    long gameId;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestUserUtil testUserUtil;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserLibraryRepository userLibraryRepository;

    @BeforeEach
    void createAndLogin() {
        testUserUtil.createAndLogin("libraryTester");
    }

    @BeforeEach
    void addGame() {
        game = gameRepository.save(
                Game.builder()
                        .title("test game")
                        .creator("creator")
                        .publisher("publisher")
                        .platform("PS5")
                        .ageRating(Arrays.asList("16", "M"))
                        .category("category")
                        .description("description")
                        .yearOfPublishing(2022)
                        .addedBy("user")
                        .build()
        );

        gameId = game.getId();
    }

    @BeforeEach
    void clean() {
        userLibraryRepository.deleteAll();
    }

    @AfterEach
    void cleanGames() {
        userLibraryRepository.deleteAll();
        gameRepository.deleteAll();
    }

    @Test
    void shouldAddGameToLibrary() {
        AddGameToLibraryRequest addGameToLibraryRequest =
                new AddGameToLibraryRequest(gameId, 9.0, Status.OWNED, true);

        ResponseEntity<Void> response = restTemplate.postForEntity("http://localhost:" + port + "/library",
                addGameToLibraryRequest, void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Optional<GameInLibrary> gameInLibraryOptional = userLibraryRepository.findById(
                new GameInLibraryId("libraryTester", gameId));

        assertTrue(gameInLibraryOptional.isPresent());
    }

    @Test
    void shouldReturnGameLibrary() {
        userLibraryRepository.save(new GameInLibrary(
                "libraryTester", null, gameId, null, null, null, true));

        ResponseEntity<GameInLibraryResponse[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/library", GameInLibraryResponse[].class);

        assertNotNull(response.getBody());
        assertEquals(List.of(new GameInLibraryResponse(game, null, null, true)),
                List.of(response.getBody()));
    }

    @Test
    void shouldDeleteGameFromLibrary() {
        userLibraryRepository.save(new GameInLibrary(
                "libraryTester", null, gameId, null, null, null, true));

        restTemplate.delete("http://localhost:" + port + "/library?id=" + gameId);

        Optional<GameInLibrary> gameInLibraryOptional = userLibraryRepository.findById(
                new GameInLibraryId("libraryTester", gameId));

        assertFalse(gameInLibraryOptional.isPresent());
    }

    @Test
    void shouldUpdateGameInLibrary() {
        userLibraryRepository.save(new GameInLibrary(
                "libraryTester", null, gameId, null, null, null, true));

        UpdateGameRequest updateGameRequest = new UpdateGameRequest(9.5, Status.OWNED, true);

        restTemplate.put("http://localhost:" + port + "/library/" + gameId, updateGameRequest);

        Optional<GameInLibrary> gameInLibraryOptional = userLibraryRepository.findById(
                new GameInLibraryId("libraryTester", gameId));

        assertTrue(gameInLibraryOptional.isPresent());

        GameInLibrary gameInLibrary = gameInLibraryOptional.get();
        assertEquals(9.5, gameInLibrary.getRating());
        assertEquals(Status.OWNED, gameInLibrary.getStatus());
    }

    @Test
    void shouldFindGameInLibrary() {
        userLibraryRepository.save(new GameInLibrary(
                "libraryTester", null, gameId, null, 9.0, Status.OWNED, true));

        ResponseEntity<GameInLibraryResponse[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/library/find?status=" + Status.OWNED + "&rating=9.0&played=true",
                GameInLibraryResponse[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        GameInLibraryResponse[] responseBody = response.getBody();
        List<GameInLibraryResponse> expected = List.of(new GameInLibraryResponse(game, 9.0, Status.OWNED, true));

        assertNotNull(responseBody);
        assertEquals(expected, List.of(responseBody));
    }
}