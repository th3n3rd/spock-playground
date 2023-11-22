package com.example.spockplayground


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import spock.lang.Specification
import spock.lang.Stepwise

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@Stepwise
@SpringBootTest(
    webEnvironment = RANDOM_PORT
)
class JourneyTests extends Specification {

    @Autowired
    private TestRestTemplate client

    @Autowired
    private InMemorySecretWords secretWords

    void setup() {
        secretWords.add("correct")
    }

    def "winning gameplay journey"() {
        def player = new Player(client, "player-1", "password-1")

        player.startNewGame()
        player.receivedHint("_______")

        player.guess("first-try")
        player.receivedHint("c______")

        player.guess("second-try")
        player.receivedHint("c_____t")

        player.guess("third-try")
        player.receivedHint("co____t")

        player.guess("correct")

        expect:
        player.hasWon()
    }

    def "track performances journey"() {
        def player = new Player(client, "player-2", "password-2")

        player.startNewGame()
        player.guess("first-try")
        player.guess("correct")

        expect:
        player.checkLeaderboard([
            [playerId: "player-2", score: 50],
            [playerId: "player-1", score: 25],
        ])
    }

    def "losing gameplay journey"() {
        def player = new Player(client, "player-3", "password-3")

        player.startNewGame()
        def maxAttempts = "correct".size()
        for (i in 1..maxAttempts) {
            player.guess("next-try")
        }

        expect:
        player.receivedHint("correct")
        player.hasLost()
    }
}
