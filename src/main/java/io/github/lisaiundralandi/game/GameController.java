package io.github.lisaiundralandi.game;

import io.github.lisaiundralandi.LoginUtil;
import io.github.lisaiundralandi.user.CurrentLogin;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
public class GameController {
    private final GameRepository gameRepository;
    private final CurrentLogin currentLogin;
    private final LoginUtil loginUtil;

    public GameController(GameRepository gameRepository, CurrentLogin currentLogin,
                          LoginUtil loginUtil) {
        this.gameRepository = gameRepository;
        this.currentLogin = currentLogin;
        this.loginUtil = loginUtil;
    }

    @PostMapping(path = "/game")
    public int createGame(@RequestBody @Valid GameRequest gameRequest) {
        loginUtil.checkIfLogged();
        return gameRepository.addGame(
                Game.builder()
                        .id(gameRepository.currentId())
                        .title(gameRequest.getTitle())
                        .creator(gameRequest.getCreator())
                        .publisher(gameRequest.getPublisher())
                        .platform(gameRequest.getPlatform())
                        .ageRating(gameRequest.getAgeRating())
                        .category(gameRequest.getCategory())
                        .description(gameRequest.getDescription())
                        .yearOfPublishing(gameRequest.getYearOfPublishing())
                        .addedBy(currentLogin.getLogin())
                        .build()
        );
    }

    @GetMapping(path = "/game/{id}")
    public Game getGame(@PathVariable int id) {
        Game game = gameRepository.getGame(id);
        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            return game;
        }
    }

    @DeleteMapping(path = "/game/{id}")
    public void deleteGame(@PathVariable int id) {
        loginUtil.checkIfLogged();
        gameRepository.deleteGame(id);
    }

    @PutMapping(path = "/game/{id}")
    public void updateGame(@PathVariable int id, @RequestBody @Valid GameRequest gameRequest) {
        loginUtil.checkIfLogged();
        Game game = gameRepository.getGame(id);

        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        game.setTitle(gameRequest.getTitle());
        game.setCreator(gameRequest.getCreator());
        game.setPublisher(gameRequest.getPublisher());
        game.setPlatform(gameRequest.getPlatform());
        game.setAgeRating(gameRequest.getAgeRating());
        game.setCategory(gameRequest.getCategory());
        game.setDescription(gameRequest.getDescription());
        game.setYearOfPublishing(gameRequest.getYearOfPublishing());
    }

    @PostMapping(path = "/game/find")
    public List<Game> findGame(@RequestBody SearchQuery searchQuery) {
        Collection<Game> games = gameRepository.getAllGames();

        return games.stream()
                .filter(game -> valueContains(game.getTitle(), searchQuery.getTitle()))
                .filter(game -> valueContains(game.getCreator(), searchQuery.getCreator()))
                .filter(game -> valueContains(game.getPublisher(), searchQuery.getPublisher()))
                .filter(game -> valueContains(game.getPlatform(), searchQuery.getPlatform()))
                .filter(game -> valueContains(game.getCategory(), searchQuery.getCategory()))
                .filter(game -> searchQuery.getYearOfPublishing() == 0 ||
                        game.getYearOfPublishing() == searchQuery.getYearOfPublishing())
                .toList();
    }

    private boolean valueContains(String value, String query) {
        return query == null ||
                (!Strings.isEmpty(query)
                        && value.contains(query));
    }
}
