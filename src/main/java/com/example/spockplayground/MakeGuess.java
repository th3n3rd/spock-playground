package com.example.spockplayground;

import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
class MakeGuess {

    private final Games games;
    private final Events events;

    MakeGuess(Games games, Events events) {
        this.games = games;
        this.events = events;
    }

    Game handle(UUID gameId, String word) {
        var game = games.findById(gameId).orElseThrow(GameNotFound::new);
        var updatedGame = game.guess(word);
        events.publish(new GuessMade(updatedGame.id(), updatedGame.attempts()));
        if (updatedGame.won()) {
            events.publish(new GameWon(updatedGame.id(), updatedGame.attempts()));
        }
        return games.save(updatedGame);
    }
}
