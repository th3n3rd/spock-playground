package com.example.spockplayground;

import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
class StartNewGame {

    private final Games games;
    private final SecretWords secretWords;
    private final Events events;

    StartNewGame(Games games, SecretWords secretWords, Events events) {
        this.games = games;
        this.secretWords = secretWords;
        this.events = events;
    }

    Game handle(String playerId) {
        var secretWord = secretWords.nextWord();
        var newGame = new Game(UUID.randomUUID(), playerId, secretWord);
        events.publish(new GameStarted(newGame.id(), newGame.playerId()));
        return games.save(newGame);
    }
}
