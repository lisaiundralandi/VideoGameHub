package io.github.lisaiundralandi.user.library;

import io.github.lisaiundralandi.game.entity.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GameInLibraryResponse {
    private Game game;
    private double rating;
    private String status;
}
