package com.eventsterminal.server.config;

import com.eventsterminal.server.config.model.UserAuth;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Optional;
import java.util.logging.Logger;

@Component
public class JwtTokenProvider {

    private static final Logger LOGGER = Logger.getLogger(JwtTokenProvider.class.getName());

    @Value("${app.jwtSecret}")
    private String SECRET_KEY;

    @Value("${app.jwtExpirationTime}")
    private Long EXPIRATION_TIME;

    public String generateToken(Authentication authentication) {
        UserAuth principal = (UserAuth) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(Long.toString(principal.getId()))
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .compact();
    }

    public Long getUserAuthIdFromToken(String token) {
        if (!validateClaims(token).isPresent()) {
            return null;
        }
        Claims body = validateClaims(token).get().getBody();
        return Long.parseLong(body.getSubject());
    }

    public boolean isTokenValid(String token) {
        if (StringUtils.isEmpty(token) || StringUtils.hasText(token)) {
            return false;
        }
        return validateClaims(token).isPresent();
    }

    private Optional<Jws<Claims>> validateClaims(String token) {
        Jws<Claims> claimsJws = null;
        try {
            claimsJws = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
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
        return Optional.ofNullable(claimsJws);
    }

}
