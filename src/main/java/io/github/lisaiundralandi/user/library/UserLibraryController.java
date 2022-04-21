package io.github.lisaiundralandi.user.library;

import io.github.lisaiundralandi.LoginUtil;
import io.github.lisaiundralandi.game.Game;
import io.github.lisaiundralandi.game.GameRepository;
import org.springframework.web.bind.annotation.*;

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
        userLibraryRepository.addGame(gameRepository.getGame(request.getId()));
    }

    @GetMapping(path = "/library")
    public Set<Game> getUserLibrary() {
        loginUtil.checkIfLogged();
        return userLibraryRepository.getGames();
    }

    @DeleteMapping(path = "/library")
    public void removeGame(@RequestParam("id") int id) {
        loginUtil.checkIfLogged();
        userLibraryRepository.removeGame(gameRepository.getGame(id));
    }
}
