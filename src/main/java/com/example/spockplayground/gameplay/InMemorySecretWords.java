package com.example.spockplayground.gameplay;

import java.util.LinkedList;
import java.util.Queue;
import org.springframework.stereotype.Repository;

// TODO: remove the public modifier and instead load some data using spring profiles
@Repository
public class InMemorySecretWords implements SecretWords {

    private final Queue<String> words = new LinkedList<>();

    public void add(String word) {
        words.add(word);
    }

    @Override
    public String nextWord() {
        return words.poll();
    }
}
