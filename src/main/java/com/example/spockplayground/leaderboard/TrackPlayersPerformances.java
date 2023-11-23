package com.example.spockplayground.leaderboard;

import com.example.spockplayground.common.Events;
import com.example.spockplayground.gameplay.GameStarted;
import com.example.spockplayground.gameplay.GameWon;
import org.jmolecules.event.annotation.DomainEventHandler;
import org.springframework.stereotype.Component;

@Component
class TrackPlayersPerformances {

    private final Leaderboard leaderboard;

    TrackPlayersPerformances(Events events, Leaderboard leaderboard) {
        this.leaderboard = leaderboard;
        events.subscribe(GameStarted.class, this::on);
        events.subscribe(GameWon.class, this::on);
    }

    @DomainEventHandler
    void on(GameStarted event) {
        leaderboard.newGame(event.id(), event.playerId());
    }

    @DomainEventHandler
    void on(GameWon event) {
        leaderboard.gameWon(event.id(), event.playerId(), event.attempts());
    }
}
