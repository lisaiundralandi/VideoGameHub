package io.github.lisaiundralandi.user.library;

import io.github.lisaiundralandi.game.entity.Game;
import io.github.lisaiundralandi.user.library.entity.Status;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class GameInLibraryResponse {
    private Game game;
    private Double rating;
    private Status status;
    private boolean played;
}
