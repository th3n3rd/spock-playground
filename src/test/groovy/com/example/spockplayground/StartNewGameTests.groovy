package com.example.spockplayground

import spock.lang.Specification

class StartNewGameTests extends Specification {

    def secretWords = new InMemorySecretWords()
    def games = new InMemoryGames()
    def events = new InMemoryEvents()
    def useCase = new StartNewGame(games, secretWords, events)

    def "new games are unique"() {
        when:
        def firstGame = useCase.handle()
        def secondGame = useCase.handle()

        then:
        firstGame != secondGame
    }

    def "new games gets persisted"() {
        when:
        def newGame = useCase.handle()

        then:
        !games.findById(newGame.id()).empty
    }

    def "new games gets assigned a random secret word"() {
        given:
        secretWords.add("first")
        secretWords.add("second")

        when:
        def firstGame = useCase.handle()
        def secondGame = useCase.handle()

        then:
        firstGame.secretWord() == "first"
        secondGame.secretWord() == "second"
    }

    def "publish a new event when a new game starts"() {
        given:
        secretWords.add("dont-care")

        when:
        def game = useCase.handle()

        then:
        new GameStarted(game.id()) in events.findAll()
    }
}
