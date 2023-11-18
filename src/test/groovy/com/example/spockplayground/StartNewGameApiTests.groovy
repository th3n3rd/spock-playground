package com.example.spockplayground


import org.spockframework.spring.SpringBean
import org.spockframework.spring.SpringSpy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(StartNewGameApi)
@Import([StartNewGame, InMemoryGames, InMemorySecretWords])
class StartNewGameApiTests extends Specification {

    @Autowired
    private MockMvc client

    @SpringSpy
    private StartNewGame useCase

    def "starts a new game successfully"() {
        given:
        def newGame = new Game(UUID.fromString("00000000-0000-0000-0000-000000000000"), "secret-word")
        useCase.handle() >> newGame

        when:
        def result = client.perform(post("/games"))

        then:
        result.andExpect(status().is2xxSuccessful())
        result.andExpect(content().json("""
        {
            "id": "00000000-0000-0000-0000-000000000000"
        }
        """))
    }
}
