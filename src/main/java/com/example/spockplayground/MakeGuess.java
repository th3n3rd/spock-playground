package com.example.spockplayground;

import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
class MakeGuess {

    private final Games games;

    MakeGuess(Games games) {
        this.games = games;
    }

    Game handle(UUID gameId, String word) {
        var game = games.findById(gameId).orElseThrow(GameNotFound::new);
        var updatedGame = game.guess(word);
        return games.save(updatedGame);
    }
}
