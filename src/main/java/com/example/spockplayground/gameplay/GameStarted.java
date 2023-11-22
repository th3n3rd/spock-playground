package com.example.spockplayground.gameplay;

import java.util.UUID;
import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public record GameStarted(UUID id, String playerId) {}
