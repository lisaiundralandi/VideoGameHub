package io.github.lisaiundralandi.game;

import io.github.lisaiundralandi.LoginUtil;
import io.github.lisaiundralandi.game.entity.Game;
import io.github.lisaiundralandi.user.CurrentLogin;
import io.github.lisaiundralandi.user.entity.UserType;
import io.github.lisaiundralandi.user.library.UserLibraryRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@RestController
public class GameController {
    private final GameRepository gameRepository;
    private final CurrentLogin currentLogin;
    private final LoginUtil loginUtil;
    private final UserLibraryRepository userLibraryRepository;

    public GameController(GameRepository gameRepository, CurrentLogin currentLogin,
                          LoginUtil loginUtil,
                          UserLibraryRepository userLibraryRepository) {
        this.gameRepository = gameRepository;
        this.currentLogin = currentLogin;
        this.loginUtil = loginUtil;
        this.userLibraryRepository = userLibraryRepository;
    }

    @PostMapping(path = "/game")
    public long createGame(@RequestBody @Valid GameRequest gameRequest) {
        loginUtil.checkIfLogged();
        return gameRepository.save(
                        Game.builder()
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
                )
                .getId();
    }

    @GetMapping(path = "/game/{id}")
    public Game getGame(@PathVariable long id) {
        Optional<Game> game = gameRepository.findById(id);
        if (game.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            return game.get();
        }
    }

    @Transactional
    @DeleteMapping(path = "/game/{id}")
    public void deleteGame(@PathVariable long id) {
        loginUtil.checkAccess(UserType.ADMIN);

        userLibraryRepository.deleteAllByGameId(id);
        gameRepository.deleteById(id);
    }

    @PutMapping(path = "/game/{id}")
    public void updateGame(@PathVariable long id, @RequestBody @Valid GameRequest gameRequest) {
        loginUtil.checkAccess(UserType.ADMIN);

        Optional<Game> optionalGame = gameRepository.findById(id);

        if (optionalGame.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Game game = optionalGame.get();
        game.setTitle(gameRequest.getTitle());
        game.setCreator(gameRequest.getCreator());
        game.setPublisher(gameRequest.getPublisher());
        game.setPlatform(gameRequest.getPlatform());
        game.setAgeRating(gameRequest.getAgeRating());
        game.setCategory(gameRequest.getCategory());
        game.setDescription(gameRequest.getDescription());
        game.setYearOfPublishing(gameRequest.getYearOfPublishing());
        gameRepository.save(game);
    }

    @PostMapping(path = "/game/find")
    public List<Game> findGame(@RequestBody SearchQuery searchQuery) {
        Iterable<Game> games = gameRepository.findAll();

        return StreamSupport.stream(games.spliterator(), false)
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
