package com.example.spockplayground;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class RegisterPlayerApi {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsManager userDetailsManager;

    public RegisterPlayerApi(PasswordEncoder passwordEncoder, UserDetailsManager userDetailsManager) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsManager = userDetailsManager;
    }

    @PostMapping("/players")
    void handle(@RequestBody Credentials credentials) {
        userDetailsManager.createUser(
            User
                .withUsername(credentials.username())
                .password(credentials.password())
                .passwordEncoder(passwordEncoder::encode)
                .build()
        );
    }

    record Credentials(String username, String password) {}
}
