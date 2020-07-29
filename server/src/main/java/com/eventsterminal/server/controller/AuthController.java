package com.eventsterminal.server.controller;

import com.eventsterminal.server.config.service.UserAuthService;
import com.eventsterminal.server.domain.request.AuthenticateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserAuthService userAuthService;

    public AuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthenticateRequest request) {
        return userAuthService.login(request);
    }

    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody AuthenticateRequest request) {
        try {
            userAuthService.register(request);
           return ResponseEntity.created(new URI("http://localhost:8080")).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}

