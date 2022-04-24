package io.github.lisaiundralandi.user.library.entity;

import io.github.lisaiundralandi.game.entity.Game;
import io.github.lisaiundralandi.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(GameInLibraryId.class)
@Getter
@Setter
public class GameInLibrary {
    @Id
    @Column(name = "user_id")
    private String userId;

    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @Column(name = "game_id")
    private long gameId;

    @ManyToOne
    @MapsId("game_id")
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
    private Double rating;
    private String status;
}
