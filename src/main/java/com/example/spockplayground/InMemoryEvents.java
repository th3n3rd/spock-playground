package com.example.spockplayground;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryEvents implements Events {

    private final List<Object> events = new ArrayList<>();

    @Override
    public void publish(Object event) {
        events.add(event);
    }

    public List<Object> findAll() {
        return events;
    }
}
