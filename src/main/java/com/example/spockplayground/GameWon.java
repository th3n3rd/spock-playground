package com.example.spockplayground;

import java.util.UUID;

public record GameWon(UUID id, String playerId, int attempts) {}
