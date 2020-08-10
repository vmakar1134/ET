package com.eventsterminal.server.config.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.security.core.Authentication;

public interface TokenProvideService {

    String generateToken(Authentication authentication);

    Long getUserIdFromClaims(Jws<Claims> body);

    Jws<Claims> getClaimsJws(String token);

}
