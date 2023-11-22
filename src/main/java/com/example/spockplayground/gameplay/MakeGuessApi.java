package com.example.spockplayground.gameplay;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
class MakeGuessApi {

    private final MakeGuess useCase;

    MakeGuessApi(MakeGuess useCase) {
        this.useCase = useCase;
    }

    @PostMapping("/games/{id}/guesses")
    GuessMade handle(@PathVariable UUID id, @RequestBody Guess guess, @AuthenticationPrincipal UserDetails player) {
        var game = useCase.handle(id, player.getUsername(), guess.word());
        return new GuessMade(game.id(), game.playerId(), game.hint(), game.attempts());
    }

    @ExceptionHandler(GameForbidden.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    void onGameForbidden() {}

    @ExceptionHandler(GameAlreadyCompleted.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    void onGameAlreadyCompleted() {}

    @ExceptionHandler(GameNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    void onGameNotFound() {}

    record Guess(String word) {}
    record GuessMade(UUID id, String playerId, String hint, int attempts) {}
}
