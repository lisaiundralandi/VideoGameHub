package io.github.lisaiundralandi.user.library;

import io.github.lisaiundralandi.LoginUtil;
import io.github.lisaiundralandi.game.GameRepository;
import io.github.lisaiundralandi.game.entity.Game;
import io.github.lisaiundralandi.user.CurrentLogin;
import io.github.lisaiundralandi.user.library.entity.GameInLibrary;
import io.github.lisaiundralandi.user.library.entity.GameInLibraryId;
import io.github.lisaiundralandi.user.library.entity.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
public class UserLibraryController {
    private final UserLibraryRepository userLibraryRepository;
    private final LoginUtil loginUtil;
    private final GameRepository gameRepository;
    private final CurrentLogin currentLogin;


    public UserLibraryController(UserLibraryRepository userLibraryRepository,
                                 LoginUtil loginUtil, GameRepository gameRepository,
                                 CurrentLogin currentLogin) {
        this.userLibraryRepository = userLibraryRepository;
        this.loginUtil = loginUtil;
        this.gameRepository = gameRepository;
        this.currentLogin = currentLogin;
    }

    @PostMapping(path = "/library")
    @Operation(summary = "Dodaje grę do biblioteki użytkownika", responses = {
            @ApiResponse(responseCode = "200", description = "Gra dodana pomyślnie"),
            @ApiResponse(responseCode = "401", description = "Użytkownik niezalogowany"),
            @ApiResponse(responseCode = "404", description = "Gra nie istnieje")
    })
    public void addToLibrary(@RequestBody AddGameToLibraryRequest request) {
        loginUtil.checkIfLogged();
        Optional<Game> optionalGame = gameRepository.findById(request.getId());
        if (optionalGame.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game does not exist");
        }
        validateRating(request.getRating());
        userLibraryRepository.save(new GameInLibrary(
                currentLogin.getLogin(), null, request.getId(), optionalGame.get(), request.getRating(),
                request.getStatus(), request.isPlayed()
        ));
    }

    @GetMapping(path = "/library")
    @Operation(summary = "Zwraca całą bibliotekę użytkownika", responses = {
            @ApiResponse(responseCode = "200", description = "Zwraca listę gier"),
            @ApiResponse(responseCode = "401", description = "Użytkownik niezalogowany")
    })
    public List<GameInLibraryResponse> getUserLibrary() {
        loginUtil.checkIfLogged();
        return userLibraryRepository.findByUserId(currentLogin.getLogin())
                .stream()
                .map(gameInLibrary -> new GameInLibraryResponse(gameInLibrary.getGame(), gameInLibrary.getRating(),
                        gameInLibrary.getStatus(), gameInLibrary.isPlayed()))
                .toList();
    }

    @DeleteMapping(path = "/library")
    @Operation(summary = "Usuwa grę z biblioteki użytkownika", responses = {
            @ApiResponse(responseCode = "200", description = "Gra usunięta pomyślnie"),
            @ApiResponse(responseCode = "401", description = "Użytkownik niezalogowany"),
            @ApiResponse(responseCode = "404", description = "Gra nie jest dodana do biblioteki")
    })
    public void removeGame(@RequestParam("id") long id) {
        loginUtil.checkIfLogged();

        if (!gameRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        userLibraryRepository.deleteById(new GameInLibraryId(currentLogin.getLogin(), id));
    }

    @PutMapping(path = "/library/{gameId}")
    @Operation(summary = "Aktualizuje grę w bibliotece",
            description = "Aktualizuje ocenę i status gry w bibliotece oraz to czy w nią grano",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Gra zaktualizowana pomyślnie"),
                    @ApiResponse(responseCode = "401", description = "Użytkownik niezalogowany"),
                    @ApiResponse(responseCode = "404", description = "Gra nie jest dodana do biblioteki")
            })
    public void updateGame(@PathVariable long gameId, @RequestBody UpdateGameRequest request) {
        loginUtil.checkIfLogged();

        Optional<GameInLibrary> optionalGameInLibrary = userLibraryRepository.findById(
                new GameInLibraryId(currentLogin.getLogin(), gameId));

        if (optionalGameInLibrary.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        GameInLibrary game = optionalGameInLibrary.get();
        validateRating(request.getRating());
        game.setRating(request.getRating());
        game.setStatus(request.getStatus());
        game.setPlayed(request.isPlayed());
        userLibraryRepository.save(game);
    }

    @GetMapping(path = "/library/find")
    @Operation(summary = "Wyszukuje gry w bibliotece",
            description = "Wyszukuje gry w bibliotece na podstawie oceny, statusu i tego, czy w nią grano ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Zwraca listę gier"),
                    @ApiResponse(responseCode = "401", description = "Użytkownik niezalogowany")
            })
    public List<GameInLibraryResponse> findGamesInLibrary(
            @RequestParam(value = "status", required = false) Status status,
            @RequestParam(value = "rating", required = false) Double rating,
            @RequestParam(value = "played", required = false) Boolean played
    ) {
        loginUtil.checkIfLogged();

        List<GameInLibrary> gamesInLibrary = userLibraryRepository.findByUserId(currentLogin.getLogin());

        return gamesInLibrary.stream()
                .filter(gameInLibrary -> status == null || gameInLibrary.getStatus() == status)
                .filter(gameInLibrary -> rating == null || Objects.equals(gameInLibrary.getRating(), rating))
                .filter(gameInLibrary -> played == null || gameInLibrary.isPlayed() == played)
                .map(gameInLibrary -> new GameInLibraryResponse(gameInLibrary.getGame(), gameInLibrary.getRating(),
                        gameInLibrary.getStatus(), gameInLibrary.isPlayed()))
                .toList();
    }

    private void validateRating(Double rating) {
        if (rating != null && (rating < 0 || rating > 10)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating must be between 0 and 10");
        }
    }
}
