package com.example.spockplayground;

import static java.util.Comparator.comparingInt;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryLeaderboard implements Leaderboard {
    private final Map<String, Ranking> rankingByPlayer = new TreeMap<>();

    @Override
    public void newGame(UUID id, String playerId) {
        rankingByPlayer.putIfAbsent(playerId, new Ranking(playerId, 0));
    }

    @Override
    public void gameWon(UUID id, String playerId, int attempts) {
        rankingByPlayer.compute(playerId, (__, ranking) -> ranking.scoring(100 / attempts));
    }

    @Override
    public List<Ranking> findAll() {
        return rankingByPlayer.values()
            .stream()
            .sorted(comparingInt(Ranking::score).reversed())
            .toList();
    }

    void withRanking(String playerId, int score) {
        rankingByPlayer.put(playerId, new Ranking(playerId, score));
    }
}
