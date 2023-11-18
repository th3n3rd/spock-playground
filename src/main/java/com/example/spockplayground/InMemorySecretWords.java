package com.example.spockplayground;

import java.util.LinkedList;
import java.util.Queue;

class InMemorySecretWords implements SecretWords {

    private final Queue<String> words = new LinkedList<>();

    public void add(String word) {
        words.add(word);
    }

    @Override
    public String nextWord() {
        return words.poll();
    }
}
