package com.example.spockplayground;

import java.util.UUID;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class StartNewGameApi {

    private final StartNewGame useCase;

    StartNewGameApi(StartNewGame useCase) {
        this.useCase = useCase;
    }

    @PostMapping("/games")
    NewGame handle() {
        var newGame = useCase.handle();
        return new NewGame(newGame.id());
    }

    record NewGame(UUID id) {}
}
