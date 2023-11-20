package com.example.spockplayground

import spock.lang.Specification

import static com.example.spockplayground.GamesMother.*
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
        game = useCase.handle(game.id(), game.playerId(), "incorrect")
        game = useCase.handle(game.id(), game.playerId(), game.secretWord())

        then:
        game.attempts() == 2
    }

    def "successful guesses record a win in the game"() {
        given:
        def game = games.save(newGame())

        when:
        game = useCase.handle(game.id(), game.playerId(), game.secretWord())

        then:
        game.won()
    }

    def "making a guess on a completed game is not allowed"() {
        given:
        def game = games.save(wonGame())

        when:
        useCase.handle(game.id(), game.playerId(), game.secretWord())

        then:
        thrown(GameAlreadyCompleted)
    }

    def "making a guess on a game started by another player is not allowed"() {
        given:
        def game = games.save(newGame("player-1"))

        when:
        useCase.handle(game.id(), "player-2", game.secretWord())

        then:
        thrown(GameForbidden)
    }

    def "fail to make a guess for a non-existing game"() {
        when:
        useCase.handle(anyGameId(), "dont-care", "dont-care")

        then:
        thrown(GameNotFound)
    }

    def "publish a new event when a new guess is made"() {
        given:
        def game = games.save(newGame("some-player"))

        when:
        game = useCase.handle(game.id(), game.playerId(), "dont-care")

        then:
        [
            new GuessMade(game.id(), "some-player", 1)
        ] == events.findAll()
    }

    def "publish a new event when the correct guess is made and the game is won"() {
        given:
        def game = games.save(newGame("some-player"))

        when:
        game = useCase.handle(game.id(), game.playerId(), game.secretWord())

        then:
        [
            new GuessMade(game.id(), "some-player", 1),
            new GameWon(game.id(), "some-player", 1)
        ] == events.findAll()
    }
}
