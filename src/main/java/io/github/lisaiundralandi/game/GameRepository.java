package io.github.lisaiundralandi.game;

import io.github.lisaiundralandi.game.Game;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GameRepository {
    private final Map<Integer, Game> games = new HashMap<>();
    private int id = 1;

    public int addGame(Game game) {
        games.put(id, game);
        int lastId = id;
        id++;
        return lastId;
    }

    public int currentId() {
        return id;
    }

    public Game getGame(int id) {
        return games.get(id);
    }

    public void deleteGame(int id) {
        games.remove(id);
    }

    public Collection<Game> getAllGames() {
        return games.values();
    }
}
