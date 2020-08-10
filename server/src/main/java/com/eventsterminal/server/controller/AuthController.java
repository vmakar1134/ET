package com.eventsterminal.server.controller;

import com.eventsterminal.server.config.model.UserAuth;
import com.eventsterminal.server.config.service.GoogleAuthService;
import com.eventsterminal.server.config.service.UserAuthService;
import com.eventsterminal.server.domain.request.AuthenticateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("auth")
public class AuthController {

    private final GoogleAuthService googleAuthService;

    private final UserAuthService userAuthService;

    public AuthController(GoogleAuthService googleAuthService, UserAuthService userAuthService) {
        this.googleAuthService = googleAuthService;
        this.userAuthService = userAuthService;
    }

    @GetMapping("google")
    public void googleAuth(HttpServletRequest request, HttpServletResponse response) {
        String token = googleAuthService.authenticate(request);
        response.addHeader("Authentication", "Bearer " + token);
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody AuthenticateRequest authenticateRequest,
                                        HttpServletRequest servletRequest) {
        String token = userAuthService.login(servletRequest, authenticateRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("register")
    public ResponseEntity register(@RequestBody AuthenticateRequest request) {
        UserAuth register = userAuthService.register(request);
        return ResponseEntity.ok(register);
    }

}
