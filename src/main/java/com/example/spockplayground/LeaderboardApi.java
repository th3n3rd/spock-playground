package com.example.spockplayground;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class LeaderboardApi {

    private final Leaderboard leaderboard;

    LeaderboardApi(Leaderboard leaderboard) {
        this.leaderboard = leaderboard;
    }

    @GetMapping("/leaderboard")
    LeaderboardDetails handle() {
        return new LeaderboardDetails(
            leaderboard.findAll()
                .stream()
                .map(it -> new LeaderboardDetails.Ranking(it.playerId(), it.score()))
                .toList()
        );
    }

    record LeaderboardDetails(List<Ranking> rankings) {
        record Ranking(String playerId, int score) {}
    }
}
