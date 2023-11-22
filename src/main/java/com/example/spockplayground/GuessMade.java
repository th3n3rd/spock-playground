package com.example.spockplayground;

import java.util.UUID;

public record GuessMade(UUID id, String playerId, int attempts) {}
