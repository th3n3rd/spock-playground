package com.example.spockplayground

import org.spockframework.spring.SpringSpy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static com.example.spockplayground.GamesMother.newGame
import static com.example.spockplayground.GamesMother.testGameId
import static com.example.spockplayground.GamesMother.wonGame
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(MakeGuessApi)
@Import([MakeGuess, InMemoryGames])
class MakeGuessApiTests extends Specification {

    @Autowired
    private MockMvc client

    @Autowired
    private InMemoryGames games

    def "make any guess"() {
        given:
        def game = games.save(newGame())

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
            "attempts": 1
        }
        """))
    }

    def "make a guess on a completed game is not allowed"() {
        given:
        def game = games.save(wonGame())

        when:
        def result = client.perform(
            post("/games/{id}/guesses", game.id())
                .content("""{ "word": "${game.secretWord()}" }""")
                .contentType("application/json")
        )

        then:
        result.andExpect(status().is4xxClientError())
    }
}
