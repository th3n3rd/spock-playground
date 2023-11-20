package com.example.spockplayground

class GamesMother {

    static UUID testGameId = UUID.fromString("00000000-0000-0000-0000-000000000000")
    static String testPlayerId = "some-player"
    static String testSecretWord = "secret-word"

    static def anyGameId() {
        return UUID.randomUUID()
    }

    static def newGame(String playerId = testPlayerId) {
        return new Game(testGameId, playerId, testSecretWord)
    }

    static def wonGame(String playerId = testPlayerId) {
        return new Game(testGameId, playerId, testSecretWord, 1, true)
    }

    static def lostGame() {
        return new Game(testGameId, testPlayerId, testSecretWord, testSecretWord.size(), false)
    }
}
