package com.example.spockplayground;

import java.util.UUID;
import org.springframework.http.HttpStatus;
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
    GuessMade handle(@PathVariable UUID id, @RequestBody Guess guess) {
        var game = useCase.handle(id, guess.word());
        return new GuessMade(game.id(), game.attempts());
    }

    @ExceptionHandler(GameAlreadyCompleted.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    void onGameAlreadyCompleted() {}

    @ExceptionHandler(GameNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    void onGameNotFound() {}

    record Guess(String word) {}
    record GuessMade(UUID id, int attempts) {}
}
