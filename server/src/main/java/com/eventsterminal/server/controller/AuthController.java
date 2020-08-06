package com.eventsterminal.server.controller;

import com.eventsterminal.server.config.GoogleAuth;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.nimbusds.oauth2.sdk.AuthorizationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("auth")
public class AuthController {

    private final GoogleAuth googleAuth;

    public AuthController(GoogleAuth googleAuth) {
        this.googleAuth = googleAuth;
    }

    @GetMapping("google")
    public ResponseEntity googleAuth(HttpServletRequest request) throws GeneralSecurityException, IOException {
        String id_token = request.getHeader("ID_TOKEN");
        GoogleIdToken verify = googleAuth.verify(id_token);
        return null;
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody AuthorizationRequest authorizationRequest){
        return null;
    }

    @PostMapping("register")
    public ResponseEntity register(@RequestBody AuthorizationRequest authorizationRequest){
        return null;
    }

}
