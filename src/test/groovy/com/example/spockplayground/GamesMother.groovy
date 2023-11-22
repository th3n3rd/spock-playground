package com.example.spockplayground

class GamesMother {

    static UUID testGameId = UUID.fromString("00000000-0000-0000-0000-000000000000")
    static String testPlayerId = "some-player"
    static String testSecretWord = "secret-word"

    static def anyGameId() {
        return UUID.randomUUID()
    }

    static def nonExistingGameId() {
        return anyGameId();
    }

    static def newGame() {
        return GameBuilder
            .builder()
            .id(testGameId)
            .playerId(testPlayerId)
            .secretWord(testSecretWord)
    }

    static def wonGame() {
        return newGame()
            .attempts(1)
            .won(true)
    }

    static def lostGame() {
        return newGame().with {
            it.attempts(it.secretWord().size())
        }
    }
}
