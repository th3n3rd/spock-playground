package com.example.spockplayground.gameplay;

import java.util.Optional;
import java.util.UUID;

interface Games {
    Game save(Game game);
    Optional<Game> findById(UUID id);
}
