package com.example.spockplayground.gameplay;

import java.util.UUID;
import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public record GuessMade(UUID id, String playerId, int attempts) {}
