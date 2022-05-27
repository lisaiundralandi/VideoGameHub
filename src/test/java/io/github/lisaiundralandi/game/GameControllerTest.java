package io.github.lisaiundralandi.game;

import io.github.lisaiundralandi.TestUserUtil;
import io.github.lisaiundralandi.game.entity.Game;
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
class GameControllerTest {

    @Autowired
    private TestUserUtil testUserUtil;

    @Autowired
    private GameRepository gameRepository;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void clean() {
        gameRepository.deleteAll();
    }

    @Test
    void shouldReturnGame() {
        Game game = gameRepository.save(
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

        ResponseEntity<Game> response = restTemplate.getForEntity("http://localhost:" + port + "/game/" + game.getId(),
                Game.class);
        HttpStatus responseStatusCode = response.getStatusCode();

        assertEquals(HttpStatus.OK, responseStatusCode);
        assertEquals(game, response.getBody());
    }

    @Test
    void shouldFindGame() {
        Game game = gameRepository.save(
                Game.builder()
                        .title("test game 2")
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

        SearchQuery searchQuery = SearchQuery.builder()
                .platform("PS5")
                .build();

        ResponseEntity<Game[]> response = restTemplate.postForEntity("http://localhost:" + port + "/game/find",
                searchQuery, Game[].class);
        HttpStatus responseStatusCode = response.getStatusCode();

        assertEquals(HttpStatus.OK, responseStatusCode);
        assertNotNull(response.getBody());
        assertEquals(List.of(game), List.of(response.getBody()));
    }

    @Test
    void shouldReturnNotFoundIfGameDoesNotExist() {
        ResponseEntity<Game> response = restTemplate.getForEntity("http://localhost:" + port + "/game/2",
                Game.class);
        HttpStatus responseStatusCode = response.getStatusCode();

        assertEquals(HttpStatus.NOT_FOUND, responseStatusCode);
    }

    @Test
    void shouldReturnEmptyList() {
        SearchQuery searchQuery = SearchQuery.builder()
                .platform("PS5")
                .build();

        ResponseEntity<Game[]> response = restTemplate.postForEntity("http://localhost:" + port + "/game/find",
                searchQuery, Game[].class);
        HttpStatus responseStatusCode = response.getStatusCode();

        assertEquals(HttpStatus.OK, responseStatusCode);
        assertNotNull(response.getBody());
        assertEquals(List.of(), List.of(response.getBody()));
    }

    @Test
    void shouldAddGame() {
        testUserUtil.createAndLogin("testerForAdding");

        GameRequest gameRequest = GameRequest.builder()
                .title("test game 3")
                .creator("creator")
                .publisher("publisher")
                .platform("PS5")
                .ageRating(Arrays.asList("16", "M"))
                .category("category")
                .description("description")
                .yearOfPublishing(2022)
                .build();

        ResponseEntity<Long> response = restTemplate.postForEntity("http://localhost:" + port + "/game",
                gameRequest, long.class);

        HttpStatus responseStatusCode = response.getStatusCode();
        assertEquals(HttpStatus.OK, responseStatusCode);

        Long responseBody = response.getBody();
        assertNotNull(responseBody);
        Optional<Game> optionalGame = gameRepository.findById(responseBody);
        assertTrue(optionalGame.isPresent());

        assertEquals("testerForAdding", optionalGame.get()
                .getAddedBy());
    }

    @Test
    void shouldDeleteGame() {
        testUserUtil.createAndLoginAdmin("testerForDeleting");

        Game game = gameRepository.save(
                Game.builder()
                        .title("test game 4")
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

        restTemplate.delete("http://localhost:" + port + "/game/" + game.getId());
        assertFalse(gameRepository.existsById(game.getId()));
    }


    @Test
    void shouldUpdateGame() {
        testUserUtil.createAndLoginAdmin("testerForUpdating");

        Game game = gameRepository.save(
                Game.builder()
                        .title("test game 5")
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

        GameRequest gameRequest = GameRequest.builder()
                .title("test game 5")
                .creator("creator")
                .publisher("publisher")
                .platform("PS5")
                .ageRating(Arrays.asList("18", "M"))
                .category("category")
                .description("description")
                .yearOfPublishing(2022)
                .build();

        restTemplate.put("http://localhost:" + port + "/game/" + game.getId(), gameRequest);
        Optional<Game> updateGameOptional = gameRepository.findById(game.getId());
        assertTrue(updateGameOptional.isPresent());

        Game updateGame = updateGameOptional.get();
        assertEquals(Arrays.asList("18", "M"), updateGame.getAgeRating());
    }

}