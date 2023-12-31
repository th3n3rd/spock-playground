package com.example.spockplayground.leaderboard

import com.example.spockplayground.common.InMemoryEvents
import com.example.spockplayground.gameplay.GameStarted
import com.example.spockplayground.gameplay.GameWon
import com.example.spockplayground.gameplay.GuessMade
import spock.lang.Specification

class TrackPlayersPerformancesTests extends Specification {

    def firstGameId = UUID.randomUUID()
    def secondGameId = UUID.randomUUID()
    def player1 = "player-1"
    def player2 = "player-2"

    def events = new InMemoryEvents()
    def leaderboard = new InMemoryLeaderboard()
    def useCase = new TrackPlayersPerformances(events, leaderboard)

    def "generate new rankings for all players when new games are started"() {
        when:
        events.publish(new GameStarted(firstGameId, player1))
        events.publish(new GameStarted(secondGameId, player2))

        then:
        leaderboard.findAll().size() == 2
    }

    def "ranking score is updated when the game is completed"() {
        when:
        events.publish(new GameStarted(firstGameId, player1))
        def previous = leaderboard.findAll().first().score()
        events.publish(new GuessMade(firstGameId, player1, 1))
        events.publish(new GuessMade(firstGameId, player1, 2))
        events.publish(new GameWon(firstGameId, player1, 2))

        then:
        leaderboard.findAll().first().score() > previous
    }

    def "ranking score does not change when new guesses are made"() {
        when:
        events.publish(new GameStarted(firstGameId, player1))
        def previous = leaderboard.findAll().first().score()
        events.publish(new GuessMade(firstGameId, player1, 1))
        events.publish(new GuessMade(firstGameId, player1, 2))

        then:
        leaderboard.findAll().first().score() == previous
    }
}
