package com.example.spockplayground

import org.springframework.boot.test.web.client.TestRestTemplate

class Player {

    private NewGame currentGame;
    private final TestRestTemplate client

    Player(TestRestTemplate client) {
        this.client = client
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

    boolean hasRank(int position) {
        def response = client.getForEntity("/leaderboard", Leaderboard)
        assert response.statusCode.is2xxSuccessful()
        return response.body.rankings.first().position == position
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
        int position
    }
}
