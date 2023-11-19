package com.example.spockplayground

class GamesMother {

    static UUID testGameId = UUID.fromString("00000000-0000-0000-0000-000000000000")
    static String testPlayerId = "some-player"
    static String testSecretWord = "secret-word"

    static def newGame() {
        return new Game(testGameId, testPlayerId, testSecretWord)
    }

    static def wonGame() {
        return new Game(testGameId, testSecretWord, 1, true)
    }

}
