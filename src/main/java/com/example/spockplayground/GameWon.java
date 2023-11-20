package com.example.spockplayground;

import java.util.UUID;

record GameWon(UUID id, String playerId, int attempts) {}
