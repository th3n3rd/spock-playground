package com.example.spockplayground;

import java.util.UUID;
import org.springframework.http.HttpStatus;
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
    GameDetails handle(@PathVariable UUID id) {
        var game = games.findById(id).orElseThrow(GameNotFound::new);
        return new GameDetails(game.id(), game.attempts(), game.won());
    }

    @ExceptionHandler(GameNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    void onGameNotFound() {}

    record GameDetails(UUID id, int attempts, boolean won) {}
}
