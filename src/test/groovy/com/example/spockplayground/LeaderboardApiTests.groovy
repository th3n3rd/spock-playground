package com.example.spockplayground

import com.example.spockplayground.common.WebSecurityConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(LeaderboardApi)
@Import([InMemoryEvents, InMemoryLeaderboard, TrackPlayersPerformances, WebSecurityConfig])
class LeaderboardApiTests extends Specification {

    @Autowired
    private MockMvc client;

    @Autowired
    private InMemoryLeaderboard leaderboard

    @WithSomePlayer
    def "retrieve the leaderboard successfully"() {
        given:
        leaderboard.withRanking("player-1", 125)
        leaderboard.withRanking("player-2", 75)
        leaderboard.withRanking("player-3", 150)

        when:
        def result = client.perform(get("/leaderboard"))

        then:
        result.andExpect(status().is2xxSuccessful())
        result.andExpect(content().json("""
        {
            "rankings": [
                { playerId:  "player-3", "score":  150 },
                { playerId:  "player-1", "score":  125 },
                { playerId:  "player-2", "score":  75 }
            ]
        }
        """))
    }
}
