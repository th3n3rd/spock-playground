package com.example.spockplayground

import org.springframework.boot.test.web.client.TestRestTemplate

class Player {

    private NewGame currentGame
    private String lastHint
    private final TestRestTemplate client

    Player(TestRestTemplate client, String username, String password) {
        this.client = register(client, username, password)
    }

    void startNewGame() {
        def response = client.postForEntity("/games", null, NewGame)
        assert response.statusCode.is2xxSuccessful()
        currentGame = response.body
        lastHint = currentGame.hint
    }

    void receivedHint(String expected) {
        assert lastHint == expected
    }

    void guess(String word) {
        def response = client.postForEntity("/games/{id}/guesses", [word: word], GuessMade, currentGame.id)
        assert response.statusCode.is2xxSuccessful()
        lastHint = response.body.hint
    }

    void hasWon() {
        def response = client.getForEntity("/games/{id}", GameDetails, currentGame.id)
        assert response.statusCode.is2xxSuccessful()
        assert response.body.won
    }

    void hasLost() {
        def response = client.getForEntity("/games/{id}", GameDetails, currentGame.id)
        assert response.statusCode.is2xxSuccessful()
        assert response.body.lost
    }

    void checkLeaderboard(rankings) {
        def response = client.getForEntity("/leaderboard", Leaderboard)
        assert response.statusCode.is2xxSuccessful()
        def actualRankings = response.body.rankings.collect {
            [playerId: it.playerId, score: it.score]
        }
        assert rankings == actualRankings
    }

    private static TestRestTemplate register(TestRestTemplate client, String username, String password) {
        def credentials = [username: username, password: password]
        def response = client.postForEntity("/players", credentials, Void)
        assert response.statusCode.is2xxSuccessful()
        return client.withBasicAuth(username, password)
    }

    static class NewGame {
        UUID id
        String hint
    }

    static class GuessMade {
        String hint
    }

    static class GameDetails {
        boolean won
        boolean lost
    }

    static class Leaderboard {
        List<PlayerRank> rankings
    }

    static class PlayerRank {
        String playerId
        int score
    }
}
