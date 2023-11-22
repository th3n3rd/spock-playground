package com.example.spockplayground.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryEvents implements Events {

    private final List<Object> events = new ArrayList<>();
    private final Map<Class<?>, List<Consumer<?>>> subscribers = new HashMap<>();

    @Override
    public void publish(Object event) {
        events.add(event);
        var eventSubscribers = subscribers.getOrDefault(event.getClass(), List.of());
        for (var subscriber : eventSubscribers) {
            ((Consumer<Object>) subscriber).accept(event);
        }
    }

    @Override
    public <T> void subscribe(Class<T> eventType, Consumer<T> callback) {
        subscribers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(callback);
    }

    public List<Object> findAll() {
        return events;
    }
}
