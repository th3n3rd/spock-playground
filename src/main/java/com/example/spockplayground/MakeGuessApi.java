package com.example.spockplayground;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class MakeGuessApi {

    @PostMapping("/games/{id}/guesses")
    void handle(@PathVariable String id) {

    }


}
