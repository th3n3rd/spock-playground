package com.example.spockplayground.players

import com.example.spockplayground.common.WebSecurityConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(RegisterPlayerApi)
@Import([InMemoryUserDetailsManager, WebSecurityConfig])
class RegisterPlayerApiTests extends Specification {

    @Autowired
    private UserDetailsManager userDetailsManager

    @Autowired
    private PasswordEncoder passwordEncoder

    @Autowired
    private MockMvc client

    def "adds a new application user when a player registers"() {
        when:
        def result = client.perform(
            post("/players")
                .content("""
                {
                    "username": "player-username",
                    "password": "player-password"
                }
                """)
                .contentType("application/json")
        )

        then:
        result.andExpect(status().is2xxSuccessful())
        with (userDetailsManager.loadUserByUsername("player-username")) {
            username == "player-username"
            passwordEncoder.matches("player-password", password)
        }
    }
}
