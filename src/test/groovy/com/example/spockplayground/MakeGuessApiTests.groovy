package com.example.spockplayground

import com.example.spockplayground.common.WebSecurityConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static com.example.spockplayground.GamesMother.*
import static com.example.spockplayground.GamesMother.newGame
import static com.example.spockplayground.GamesMother.wonGame
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(MakeGuessApi)
@Import([MakeGuess, InMemoryGames, InMemoryEvents, WebSecurityConfig])
class MakeGuessApiTests extends Specification {

    @Autowired
    private MockMvc client

    @Autowired
    private InMemoryGames games

    @WithPlayer
    def "make any guess"() {
        given:
        def game = games.save(newGame("some-player"))

        when:
        def result = client.perform(
            post("/games/{id}/guesses", game.id())
                .content("""{ "word": "${game.secretWord()}" }""")
                .contentType("application/json")
        )

        then:
        result.andExpect(status().is2xxSuccessful())
        result.andExpect(content().json("""
        {
            "id": "00000000-0000-0000-0000-000000000000",
            "playerId": "some-player",
            "attempts": 1
        }
        """))
    }

    @WithPlayer
    def "make a guess on a completed game is not allowed"() {
        given:
        def game = games.save(wonGame("some-player"))

        when:
        def result = client.perform(
            post("/games/{id}/guesses", game.id())
                .content("""{ "word": "${game.secretWord()}" }""")
                .contentType("application/json")
        )

        then:
        result.andExpect(status().isBadRequest())
    }

    @WithPlayer
    def "fail to make a guess for a non-existing game"() {
        when:
        def result = client.perform(
            post("/games/{id}/guesses", anyGameId())
                .content("""{ "word": "dont-care" }""")
                .contentType("application/json")
        )

        then:
        result.andExpect(status().isNotFound())
    }

    @WithPlayer
    def "make a guess non another player's game is not allowed"() {
        given:
        def game = games.save(newGame("another-player"))

        when:
        def result = client.perform(
            post("/games/{id}/guesses", game.id())
                .content("""{ "word": "${game.secretWord()}" }""")
                .contentType("application/json")
        )

        then:
        result.andExpect(status().isForbidden())
    }
}
