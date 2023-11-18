package com.example.spockplayground;

import java.util.UUID;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class StartNewGameApi {

    @PostMapping("/games")
    NewGame handle() {
        return new NewGame(UUID.randomUUID());
    }

    record NewGame(UUID id) {}
}
