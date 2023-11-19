package com.example.spockplayground;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class RegisterPlayerApi {

    @PostMapping("/players")
    void handle(@RequestBody Credentials credentials) {

    }

    record Credentials(String username, String password) {}
}
