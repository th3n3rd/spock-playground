package com.example.spockplayground;

import java.util.UUID;

record GuessMade(UUID id, String playerId, int attempts) {}
