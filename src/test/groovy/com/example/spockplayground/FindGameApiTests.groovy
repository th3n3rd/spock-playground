package com.example.spockplayground

import com.example.spockplayground.common.WebSecurityConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static com.example.spockplayground.GamesMother.newGame
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(FindGameApi)
@Import([InMemoryGames, WebSecurityConfig])
class FindGameApiTests extends Specification {

    @Autowired
    private MockMvc client

    @Autowired
    private InMemoryGames games

    @WithPlayer
    def "retrieve details of an existing game successfully"() {
        given:
        def game = games.save(newGame("some-player"))

        when:
        def result = client.perform(get("/games/{id}", game.id()))

        then:
        result.andExpect(status().is2xxSuccessful())
        result.andExpect(content().json("""
        {
            "id": "00000000-0000-0000-0000-000000000000",
            "playerId": "some-player",
            "attempts": 0,
            "won": false
        }
        """))
    }

    @WithPlayer
    def "fails to retrieve details for a non existing game"() {
        when:
        def result = client.perform(get("/games/{id}", UUID.randomUUID()))

        then:
        result.andExpect(status().isNotFound())
    }

    @WithPlayer
    def "retrieving details for another player's game is not allowed"() {
        given:
        def game = games.save(newGame("another-player"))

        when:
        def result = client.perform(get("/games/{id}", game.id()))

        then:
        result.andExpect(status().isForbidden())
    }
}
