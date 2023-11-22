package com.example.spockplayground.gameplay;

import java.util.UUID;
import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public record GameWon(UUID id, String playerId, int attempts) {}
