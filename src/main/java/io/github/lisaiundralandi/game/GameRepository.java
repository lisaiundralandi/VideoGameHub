package io.github.lisaiundralandi.game;

import io.github.lisaiundralandi.game.entity.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends CrudRepository<Game, Long> {

}
