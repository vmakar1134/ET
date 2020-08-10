package com.eventsterminal.server.config.service.impl;

import com.eventsterminal.server.config.model.Payload;
import com.eventsterminal.server.config.model.UserAuth;
import com.eventsterminal.server.config.service.GoogleAuthService;
import com.eventsterminal.server.config.service.UserAuthService;
import com.eventsterminal.server.exception.ConflictException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleAuthServiceImpl implements GoogleAuthService {

    private final HttpTransport httpRequest = new NetHttpTransport();

    private final JacksonFactory jsonFactory = new JacksonFactory();

    private final UserAuthService userAuthService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;

    public GoogleAuthServiceImpl(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @Override
    public String authenticate(HttpServletRequest request) {
        GoogleIdToken idToken = getIdToken(request);
        Payload payload = mapToPayload(idToken);
        UserAuth userAuth = userAuthService.fromPayload(payload);
        return userAuthService.socialAuthorize(request, userAuth);
    }

    private GoogleIdToken getIdToken(HttpServletRequest request) {
        String id_token = request.getHeader("id_token");
        return verifyToken(id_token);
    }

    private GoogleIdTokenVerifier getVerifier() {
        return new GoogleIdTokenVerifier.Builder(httpRequest, jsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();
    }

    private GoogleIdToken verifyToken(String idTokenString) {
        try {
            GoogleIdToken idToken = getVerifier().verify(idTokenString);
            if (idToken != null) {
                return idToken;
            }
            throw new GeneralSecurityException();
        } catch (GeneralSecurityException | IOException e) {
            throw new ConflictException("Invalid token");
        }
    }

    private Payload mapToPayload(GoogleIdToken idToken) {
        GoogleIdToken.Payload payload = idToken.getPayload();
        String userId = payload.getSubject();
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String pictureUrl = (String) payload.get("picture");
        String locale = (String) payload.get("locale");
        String familyName = (String) payload.get("family_name");
        String givenName = (String) payload.get("given_name");
        boolean emailVerified = payload.getEmailVerified();

        return Payload.builder()
                .userId(userId)
                .email(email)
                .emailVerified(emailVerified)
                .name(name)
                .familyName(familyName)
                .givenName(givenName)
                .locale(locale)
                .pictureUrl(pictureUrl)
                .build();
    }

}
