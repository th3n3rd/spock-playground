package com.example.spockplayground

import groovy.transform.Canonical
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(
    webEnvironment = RANDOM_PORT
)
class JourneyTests extends Specification {

    @Autowired
    private TestRestTemplate client;

    def "player journey"() {
        def player = new Player(client)

        player.startNewGame()
        player.guess("right-guess")

        expect:
        player.hasWon()
    }

    static class Player {

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

        static class NewGame {
            UUID id
        }

        static class GameDetails {
            boolean won
        }
    }
}
