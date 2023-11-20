package com.example.spockplayground;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class LeaderboardApi {

    @GetMapping("/leaderboard")
    Leaderboard handle() {
        return new Leaderboard(
            List.of(
                new Leaderboard.Ranking("player-2", 50),
                new Leaderboard.Ranking("player-1", 25)
            )
        );
    }

    record Leaderboard(List<Ranking> rankings) {
        record Ranking(String playerId, int score) {}
    }
}
