package io.github.lisaiundralandi.user.library;

import io.github.lisaiundralandi.LoginUtil;
import io.github.lisaiundralandi.game.GameRepository;
import io.github.lisaiundralandi.game.entity.Game;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;

@RestController
public class UserLibraryController {
    private final UserLibraryRepository userLibraryRepository;
    private final LoginUtil loginUtil;
    private final GameRepository gameRepository;


    public UserLibraryController(UserLibraryRepository userLibraryRepository,
                                 LoginUtil loginUtil, GameRepository gameRepository) {
        this.userLibraryRepository = userLibraryRepository;
        this.loginUtil = loginUtil;
        this.gameRepository = gameRepository;
    }

    @PostMapping(path = "/library")
    public void addToLibrary(@RequestBody AddGameToLibraryRequest request) {
        loginUtil.checkIfLogged();
        Optional<Game> optionalGame = gameRepository.findById(request.getId());
        if (optionalGame.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        userLibraryRepository.addGame(optionalGame.get());
    }

    @GetMapping(path = "/library")
    public Set<Game> getUserLibrary() {
        loginUtil.checkIfLogged();
        return userLibraryRepository.getGames();
    }

    @DeleteMapping(path = "/library")
    public void removeGame(@RequestParam("id") long id) {
        loginUtil.checkIfLogged();
        Optional<Game> optionalGame = gameRepository.findById(id);
        if (optionalGame.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        userLibraryRepository.removeGame(optionalGame.get());
    }
}
