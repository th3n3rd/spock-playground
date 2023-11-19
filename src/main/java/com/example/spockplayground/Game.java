package com.example.spockplayground;

import java.util.UUID;

record Game(UUID id, String playerId, String secretWord, int attempts, boolean won) {
    Game(UUID id, String secretWord) {
        this(id, "anonymous", secretWord, 0, false);
    }

    Game(UUID id, String playerId, String secretWord) {
        this(id, playerId, secretWord, 0, false);
    }

    Game(UUID id, String secretWord, int attempts, boolean won) {
        this(id, "anonymous", secretWord, attempts, won);
    }

    public Game guess(String word) {
        if (won) {
            throw new GameAlreadyCompleted();
        }
        return new Game(id, playerId, secretWord, attempts + 1, secretWord.equals(word));
    }
}
