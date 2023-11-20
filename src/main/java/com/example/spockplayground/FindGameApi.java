package com.example.spockplayground;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
class FindGameApi {

    private final Games games;

    FindGameApi(Games games) {
        this.games = games;
    }

    @GetMapping("/games/{id}")
    GameDetails handle(@PathVariable UUID id, @AuthenticationPrincipal UserDetails player) {
        var game = games.findById(id).orElseThrow(GameNotFound::new);
        if (!game.playerId().equals(player.getUsername())) {
            throw new GameForbidden();
        }
        return new GameDetails(game.id(), game.playerId(), game.attempts(), game.won());
    }

    @ExceptionHandler(GameNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    void onGameNotFound() {}

    @ExceptionHandler(GameForbidden.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    void onGameForbidden() {}

    record GameDetails(UUID id, String playerId, int attempts, boolean won) {}
}
