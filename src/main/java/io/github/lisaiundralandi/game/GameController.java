package io.github.lisaiundralandi.game;

import io.github.lisaiundralandi.LoginUtil;
import io.github.lisaiundralandi.game.entity.Game;
import io.github.lisaiundralandi.user.CurrentLogin;
import io.github.lisaiundralandi.user.entity.UserType;
import io.github.lisaiundralandi.user.library.UserLibraryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(responses = {
            @ApiResponse(responseCode = "200", description = "Gra dodana pomyślnie, zwraca identyfikator dodanej gry"),
            @ApiResponse(responseCode = "401", description = "Użytkownik niezalogowany")
    }, summary = "Dodaje grę do bazy danych")
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
    @Operation(summary = "Zwraca grę o podanym identyfikatorze", responses = {
            @ApiResponse(responseCode = "200", description = "Zwraca grę"),
            @ApiResponse(responseCode = "404", description = "Gra nie istnieje")
    })
    public Game getGame(@PathVariable long id) {
        return getGameFromRepo(id);
    }

    @Transactional
    @DeleteMapping(path = "/game/{id}")
    @Operation(summary = "Usuwa grę z bazy danych gier",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Gra usunięta pomyślnie"),
                    @ApiResponse(responseCode = "401", description = "Użytkownik niezalogowany"),
                    @ApiResponse(responseCode = "403", description = "Użytkownik nie jest administratorem"),
                    @ApiResponse(responseCode = "404", description = "Gra nie istnieje")
            })
    public void deleteGame(@PathVariable long id) {
        loginUtil.checkAccess(UserType.ADMIN);

        getGameFromRepo(id);
        userLibraryRepository.deleteAllByGameId(id);
        gameRepository.deleteById(id);
    }

    @PutMapping(path = "/game/{id}")
    @Operation(summary = "Aktualizuje szczegóły gry o podanym identyfikatorze",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Gra zaktualizowana pomyślnie"),
                    @ApiResponse(responseCode = "401", description = "Użytkownik niezalogowany"),
                    @ApiResponse(responseCode = "403", description = "Użytkownik nie jest administratorem"),
                    @ApiResponse(responseCode = "404", description = "Gra nie istnieje")
            })
    public void updateGame(@PathVariable long id, @RequestBody @Valid GameRequest gameRequest) {
        loginUtil.checkAccess(UserType.ADMIN);

        Game game = getGameFromRepo(id);
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
    @Operation(summary = "Zwraca listę gier pasujących do kryteriów")
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

    private Game getGameFromRepo(long id) {
        Optional<Game> optionalGame = gameRepository.findById(id);

        if (optionalGame.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game does not exist");
        }
        return optionalGame.get();
    }
}
