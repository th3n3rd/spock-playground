package com.example.spockplayground;

import java.util.UUID;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class StartNewGameApi {

    private final StartNewGame useCase;

    StartNewGameApi(StartNewGame useCase) {
        this.useCase = useCase;
    }

    @PostMapping("/games")
    NewGame handle(@AuthenticationPrincipal UserDetails player) {
        var newGame = useCase.handle(player.getUsername());
        return new NewGame(newGame.id(), newGame.playerId());
    }

    record NewGame(UUID id, String playerId) {}
}
