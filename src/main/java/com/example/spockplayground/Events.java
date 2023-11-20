package com.example.spockplayground;

import java.util.function.Consumer;

interface Events {
    void publish(Object event);
    <T> void subscribe(Class<T> eventType, Consumer<T> callback);
}
