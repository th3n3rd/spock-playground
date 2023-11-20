package com.example.spockplayground;

record Ranking(String playerId, int score) {
    Ranking scoring(int deltaScore) {
        return new Ranking(playerId, score + deltaScore);
    }
}
