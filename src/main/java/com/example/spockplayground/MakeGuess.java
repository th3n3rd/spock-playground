package com.example.spockplayground;

import com.example.spockplayground.common.Events;
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

    Game handle(UUID gameId, String playerId, String word) {
        var game = games.findById(gameId).orElseThrow(GameNotFound::new);
        var updatedGame = game.guess(playerId, word);
        events.publish(new GuessMade(updatedGame.id(), updatedGame.playerId(), updatedGame.attempts()));
        if (updatedGame.won()) {
            events.publish(new GameWon(updatedGame.id(), updatedGame.playerId(), updatedGame.attempts()));
        }
        return games.save(updatedGame);
    }
}
