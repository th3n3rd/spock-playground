package com.example.spockplayground;

import io.soabase.recordbuilder.core.RecordBuilder;
import java.util.UUID;

@RecordBuilder
record Game(UUID id, String playerId, String secretWord, int attempts, boolean won) {

    Game(UUID id, String playerId, String secretWord) {
        this(id, playerId, secretWord, 0, false);
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

    String hint() {
        return new HintProgression(secretWord).nextHint(attempts);
    }
}
