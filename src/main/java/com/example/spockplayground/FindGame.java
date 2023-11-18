package com.example.spockplayground;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
class FindGame {

    @GetMapping("/games/{id}")
    GameDetails handle(@PathVariable String id) {
        return new GameDetails(true);
    }

    record GameDetails(boolean won) {}
}
