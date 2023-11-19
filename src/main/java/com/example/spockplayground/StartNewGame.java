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

    Game handle() {
        var secretWord = secretWords.nextWord();
        var newGame = new Game(UUID.randomUUID(), secretWord);
        events.publish(new GameStarted(newGame.id()));
        return games.save(newGame);
    }
}
