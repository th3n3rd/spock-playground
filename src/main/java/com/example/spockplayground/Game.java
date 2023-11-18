package com.example.spockplayground;

import java.util.UUID;

record Game(UUID id, String secretWord, int attempts, boolean won) {
    Game(UUID id, String secretWord) {
        this(id, secretWord, 0, false);
    }

    public Game guess(String word) {
        if (won) {
            throw new GameAlreadyCompleted();
        }
        return new Game(id, secretWord, attempts + 1, secretWord.equals(word));
    }
}
