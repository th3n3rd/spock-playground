package com.example.spockplayground

import com.example.spockplayground.common.WebSecurityConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static com.example.spockplayground.GamesMother.*
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

    @WithSomePlayer
    def "make any guess"() {
        given:
        def game = games.save(newGame().playerId("some-player").build())

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
            "hint": "s__________",
            "attempts": 1
        }
        """))
    }

    @WithSomePlayer
    def "make a guess on a completed game is not allowed"() {
        given:
        def game = games.save(wonGame().playerId("some-player").build())

        when:
        def result = client.perform(
            post("/games/{id}/guesses", game.id())
                .content("""{ "word": "${game.secretWord()}" }""")
                .contentType("application/json")
        )

        then:
        result.andExpect(status().isBadRequest())
    }

    @WithSomePlayer
    def "fail to make a guess for a non-existing game"() {
        when:
        def result = client.perform(
            post("/games/{id}/guesses", nonExistingGameId())
                .content("""{ "word": "dont-care" }""")
                .contentType("application/json")
        )

        then:
        result.andExpect(status().isNotFound())
    }

    @WithSomePlayer
    def "make a guess on another player's game is not allowed"() {
        given:
        def game = games.save(newGame().playerId("another-player").build())

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
