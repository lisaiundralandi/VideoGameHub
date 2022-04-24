package io.github.lisaiundralandi.user.library;

import io.github.lisaiundralandi.LoginUtil;
import io.github.lisaiundralandi.game.GameRepository;
import io.github.lisaiundralandi.game.entity.Game;
import io.github.lisaiundralandi.user.CurrentLogin;
import io.github.lisaiundralandi.user.library.entity.GameInLibrary;
import io.github.lisaiundralandi.user.library.entity.GameInLibraryId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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
    public void addToLibrary(@RequestBody AddGameToLibraryRequest request) {
        loginUtil.checkIfLogged();
        Optional<Game> optionalGame = gameRepository.findById(request.getId());
        if (optionalGame.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        userLibraryRepository.save(new GameInLibrary(
                currentLogin.getLogin(), null, request.getId(), optionalGame.get(), request.getRating(),
                request.getStatus()
        ));
    }

    @GetMapping(path = "/library")
    public List<GameInLibraryResponse> getUserLibrary() {
        loginUtil.checkIfLogged();
        return userLibraryRepository.findByUserId(currentLogin.getLogin())
                .stream()
                .map(gameInLibrary -> new GameInLibraryResponse(gameInLibrary.getGame(), gameInLibrary.getRating(),
                        gameInLibrary.getStatus()))
                .toList();
    }

    @DeleteMapping(path = "/library")
    public void removeGame(@RequestParam("id") long id) {
        loginUtil.checkIfLogged();

        if (!gameRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        userLibraryRepository.deleteById(new GameInLibraryId(currentLogin.getLogin(), id));
    }

    @PutMapping(path = "/library/{gameId}")
    public void updateGame(@PathVariable long gameId, @RequestBody UpdateGameRequest request) {

        Optional<GameInLibrary> optionalGameInLibrary = userLibraryRepository.findById(
                new GameInLibraryId(currentLogin.getLogin(), gameId));

        if (optionalGameInLibrary.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        GameInLibrary game = optionalGameInLibrary.get();
        game.setRating(request.getRating());
        game.setStatus(request.getStatus());
        userLibraryRepository.save(game);
    }
}
