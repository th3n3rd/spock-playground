package com.example.spockplayground.gameplay;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryGames implements Games {

    private final Map<UUID, Game> games = new HashMap<>();

    @Override
    public Game save(Game game) {
        games.put(game.id(), game);
        return game;
    }

    @Override
    public Optional<Game> findById(UUID id) {
        return Optional.ofNullable(games.get(id));
    }
}
