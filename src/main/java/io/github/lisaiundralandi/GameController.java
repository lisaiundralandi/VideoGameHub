package io.github.lisaiundralandi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
public class GameController {
    private final GameRepository gameRepository;

    public GameController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @PostMapping(path = "/game")
    public int createGame(@RequestBody @Valid GameRequest gameRequest) {
        return gameRepository.addGame(
                new Game(gameRepository.currentId(), gameRequest.getTitle(), gameRequest.getCreator(),
                        gameRequest.getPublisher(), gameRequest.getYearOfPublishing(), gameRequest.getAgeRating(),
                        gameRequest.getCategory(), gameRequest.getDescription()));
    }

    @GetMapping(path = "/game/{id}")
    public Game getGame(@PathVariable int id) {
        Game game = gameRepository.getGame(id);
        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            return game;
        }
    }

    @DeleteMapping(path = "/game/{id}")
    public void deleteGame(@PathVariable int id) {
        gameRepository.deleteGame(id);
    }


}
