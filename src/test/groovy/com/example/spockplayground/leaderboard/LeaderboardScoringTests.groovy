package com.example.spockplayground.leaderboard

import spock.lang.Specification

class LeaderboardScoringTests extends Specification {

    def anyGameId = UUID.randomUUID()
    def anotherGameId = UUID.randomUUID()
    def player1 = "player-1"
    def player2 = "player-2"
    def leaderboard = new InMemoryLeaderboard()

    def "new ranking starts a score of zero"() {
        when:
        leaderboard.newGame(anyGameId, player1)

        then:
        [new Ranking(player1, 0)] == leaderboard.findAll()
    }

    def "ranking score is calculated by 100 divided by the number of attempts"(int attempts, int score) {
        when:
        leaderboard.newGame(anyGameId, player1)
        leaderboard.gameWon(anyGameId, player1, attempts)

        then:
        [new Ranking(player1, score)] == leaderboard.findAll()

        where:
        attempts | score
        1        | 100
        2        | 50
        3        | 33
        4        | 25
        5        | 20
        100      | 1
    }

    def "new scores are added on top of the existing one"() {
        when:
        leaderboard.newGame(anyGameId, player1)
        leaderboard.gameWon(anyGameId, player1, 1)

        and:
        leaderboard.newGame(anotherGameId, player1)
        leaderboard.gameWon(anotherGameId, player1, 2)

        then:
        [new Ranking(player1, 150)] == leaderboard.findAll()
    }

    def "rankings are ordered by highest score first"() {
        when:
        leaderboard.newGame(anyGameId, player2)
        leaderboard.gameWon(anyGameId, player2, 1)

        and:
        leaderboard.newGame(anotherGameId, player1)
        leaderboard.gameWon(anotherGameId, player1, 2)

        then:
        [
            new Ranking(player2, 100),
            new Ranking(player1, 50)
        ] == leaderboard.findAll()
    }
}
