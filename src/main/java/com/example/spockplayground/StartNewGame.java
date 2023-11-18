package com.example.spockplayground;

import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
class StartNewGame {

    private final Games games;
    private final SecretWords secretWords;

    StartNewGame(Games games, SecretWords secretWords) {
        this.games = games;
        this.secretWords = secretWords;
    }

    Game handle() {
        var secretWord = secretWords.nextWord();
        var newGame = new Game(UUID.randomUUID(), secretWord);
        games.save(newGame);
        return newGame;
    }
}
