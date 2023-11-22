package com.example.spockplayground.gameplay;

import java.util.UUID;

public record GuessMade(UUID id, String playerId, int attempts) {}
