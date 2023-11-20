package com.example.spockplayground

import org.springframework.boot.test.web.client.TestRestTemplate

class Player {

    private NewGame currentGame;
    private final TestRestTemplate client

    Player(TestRestTemplate client, String username, String password) {
        this.client = register(client, username, password)
    }

    void startNewGame() {
        def response = client.postForEntity("/games", null, NewGame)
        assert response.statusCode.is2xxSuccessful()
        currentGame = response.body
    }

    void guess(String word) {
        def response = client.postForEntity("/games/{id}/guesses", [word: word], Void, currentGame.id)
        assert response.statusCode.is2xxSuccessful()
    }

    boolean hasWon() {
        def response = client.getForEntity("/games/{id}", GameDetails, currentGame.id)
        assert response.statusCode.is2xxSuccessful()
        return response.body.won
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
    }

    static class GameDetails {
        boolean won
    }

    static class Leaderboard {
        List<PlayerRank> rankings
    }

    static class PlayerRank {
        String playerId
        int score
    }
}
