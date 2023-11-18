package com.example.spockplayground;

import java.util.Optional;
import java.util.UUID;

interface Games {
    void save(Game game);
    Optional<Game> findById(UUID id);
}
