package io.github.lisaiundralandi.game;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GameControllerTest {

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

}