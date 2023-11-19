package com.example.spockplayground

import spock.lang.Specification

import static com.example.spockplayground.GamesMother.newGame
import static com.example.spockplayground.GamesMother.wonGame

class MakeGuessTests extends Specification {

    def events = new InMemoryEvents()
    def games = new InMemoryGames()
    def useCase = new MakeGuess(games, events)

    def "any new guess increases the recorded attempts in a game by one"() {
        given:
        def game = games.save(newGame())

        when:
        game = useCase.handle(game.id(), "incorrect")
        game = useCase.handle(game.id(), game.secretWord())

        then:
        game.attempts() == 2
    }

    def "successful guesses record a win in the game"() {
        given:
        def game = games.save(newGame())

        when:
        game = useCase.handle(game.id(), game.secretWord())

        then:
        game.won()
    }

    def "making a guess on a completed game is not allowed"() {
        given:
        def game = games.save(wonGame())

        when:
        useCase.handle(game.id(), game.secretWord())

        then:
        thrown(GameAlreadyCompleted)
    }

    def "fail to make a guess for a non-existing game"() {
        when:
        useCase.handle(UUID.randomUUID(), "dont-care")

        then:
        thrown(GameNotFound)
    }

    def "publish a new event when a new guess is made"() {
        given:
        def game = games.save(newGame())

        when:
        game = useCase.handle(game.id(), "dont-care")

        then:
        [
            new GuessMade(game.id(), 1)
        ] == events.findAll()
    }

    def "publish a new event when the correct guess is made and the game is won"() {
        given:
        def game = games.save(newGame())

        when:
        game = useCase.handle(game.id(), game.secretWord())

        then:
        [
            new GuessMade(game.id(), 1),
            new GameWon(game.id(), 1)
        ] == events.findAll()
    }
}
