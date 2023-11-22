package com.example.spockplayground.leaderboard;

import java.util.List;
import java.util.UUID;

interface Leaderboard {
    void newGame(UUID id, String playerId);
    void gameWon(UUID id, String playerId, int attempts);
    List<Ranking> findAll();
}
