package com.example.spockplayground;

import com.example.spockplayground.common.Events;
import org.springframework.stereotype.Component;

@Component
class TrackPlayersPerformances {

    private final Leaderboard leaderboard;

    TrackPlayersPerformances(Events events, Leaderboard leaderboard) {
        this.leaderboard = leaderboard;
        events.subscribe(GameStarted.class, this::on);
        events.subscribe(GameWon.class, this::on);
    }

    void on(GameStarted event) {
        leaderboard.newGame(event.id(), event.playerId());
    }

    void on(GameWon event) {
        leaderboard.gameWon(event.id(), event.playerId(), event.attempts());
    }
}
