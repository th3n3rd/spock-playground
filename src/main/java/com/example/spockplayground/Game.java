package com.example.spockplayground;

import java.util.UUID;

record Game(UUID id, String playerId, String secretWord, int attempts, boolean won, String hint) {
    Game(UUID id, String playerId, String secretWord) {
        this(id, playerId, secretWord, 0, false);
    }

    Game(UUID id, String playerId, String secretWord, int attempts, boolean won) {
        this(id, playerId, secretWord, attempts, won, "_".repeat(secretWord.length()));
    }

    public Game guess(String playerId, String word) {
        if (!playerId().equals(playerId)) {
            throw new GameForbidden();
        }
        if (won || lost()) {
            throw new GameAlreadyCompleted();
        }
        return new Game(id, playerId, secretWord, attempts + 1, secretWord.equals(word));
    }

    boolean lost() {
        return attempts >= secretWord.length();
    }
}
