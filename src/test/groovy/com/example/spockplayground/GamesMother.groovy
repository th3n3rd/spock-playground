package com.example.spockplayground

class GamesMother {

    static def newGame() {
        return new Game(UUID.fromString("00000000-0000-0000-0000-000000000000"), "secret-word")
    }

}
