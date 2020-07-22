package com.eventsterminal.server.config;

import com.eventsterminal.server.config.model.UserPrincipal;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.logging.Logger;

@Component
public class TokenProvider {

    private static final Logger LOGGER = Logger.getLogger(TokenProvider.class.getName());

    @Value("${app.jwtSecret}")
    private String SECRET_KEY;

    @Value("${app.jwtExpirationTime}")
    private Long EXPIRATION_TIME;

    public String generateToken(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(Long.toString(principal.getId()))
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .compact();
    }

    public Long getUserAuthIdFromToken(String token) {
        Claims body = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(body.getSubject());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            LOGGER.warning("Invalid jwt signature");
        } catch (MalformedJwtException e) {
            LOGGER.warning("Invalid jwt token");
        } catch (ExpiredJwtException e) {
            LOGGER.warning("Expired jwt token");
        } catch (UnsupportedJwtException e) {
            LOGGER.warning("Unsupported jwt token");
        } catch (IllegalArgumentException e) {
            LOGGER.warning("JWT claims string is empty");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
