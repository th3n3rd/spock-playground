package com.example.spockplayground.common;

import java.util.function.Consumer;

public interface Events {
    void publish(Object event);
    <T> void subscribe(Class<T> eventType, Consumer<T> callback);
}
