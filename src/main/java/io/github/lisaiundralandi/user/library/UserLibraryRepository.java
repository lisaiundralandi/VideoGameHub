package io.github.lisaiundralandi.user.library;

import io.github.lisaiundralandi.game.Game;
import io.github.lisaiundralandi.user.CurrentLogin;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserLibraryRepository {
    private final Map<String, HashSet<Game>> userLibrary = new HashMap<>();
    private final CurrentLogin currentLogin;

    public UserLibraryRepository(CurrentLogin currentLogin) {
        this.currentLogin = currentLogin;
    }

    public void addGame(Game game) {
        HashSet<Game> games = userLibrary.getOrDefault(currentLogin.getLogin(), new HashSet<>());
        games.add(game);
        userLibrary.put(currentLogin.getLogin(), games);
    }

    public HashSet<Game> getGames() {
        return userLibrary.get(currentLogin.getLogin());
    }

    public void removeGame(Game game) {
        HashSet<Game> games = userLibrary.getOrDefault(currentLogin.getLogin(), new HashSet<>());
        games.remove(game);
        userLibrary.put(currentLogin.getLogin(), games);
    }
}
