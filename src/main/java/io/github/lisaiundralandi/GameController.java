package io.github.lisaiundralandi;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class GameController {

    @PostMapping(path = "/game")
    public void createGame(@RequestBody @Valid GameRequest gameRequest) {

    }

}
