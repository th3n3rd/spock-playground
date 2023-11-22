package com.example.spockplayground

import spock.lang.Specification

import static com.example.spockplayground.GamesMother.*

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

    def "any new guess generates a better hint for the game"() {
        given:
        def game = games.save(newGame())

        when:
        def firstHint = useCase.handle(game.id(), game.playerId(), "first-try").hint()
        def secondHint = useCase.handle(game.id(), game.playerId(), "second-try").hint()

        then:
        firstHint.size() > 0
        secondHint.count("_") < firstHint.count("_")
    }

    def "successful guesses record a win in the game"() {
        given:
        def game = games.save(newGame())

        when:
        game = useCase.handle(game.id(), game.playerId(), game.secretWord())

        then:
        game.won()
    }

    def "exceeding the max number of attempts record a lost in the game"() {
        given:
        def game = games.save(newGame())

        when:
        for (i in game.secretWord()) {
            game = useCase.handle(game.id(), game.playerId(), "incorrect")
        }

        then:
        game.lost()
    }

    def "making a guess on an already won game is not allowed"() {
        given:
        def game = games.save(wonGame())

        when:
        useCase.handle(game.id(), game.playerId(), game.secretWord())

        then:
        thrown(GameAlreadyCompleted)
    }

    def "making a guess on an already lost game is not allowed"() {
        given:
        def game = games.save(lostGame())

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
