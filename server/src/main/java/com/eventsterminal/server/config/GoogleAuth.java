package com.eventsterminal.server.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Component
public class GoogleAuth {

    private HttpTransport httpRequest = new NetHttpTransport();

    private JacksonFactory jsonFactory = new JacksonFactory();

    private GoogleIdTokenVerifier verifier;

    // TODO: 8/6/20 to properties
    private final String CLIENT_ID = "474387750961-ochjmdj1lils7tt350h8oo9ul25hkla6.apps.googleusercontent.com";


    {
        verifier = new GoogleIdTokenVerifier.Builder(httpRequest, jsonFactory)
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(CLIENT_ID))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();
    }

    public GoogleIdToken verify(String idTokenString) throws GeneralSecurityException, IOException {
        return verifier.verify(idTokenString);
    }

    public void parseToken(GoogleIdToken idToken) {
        if (idToken != null) {
            Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            // Get profile information from payload
            String email = payload.getEmail();
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");
        } else {
            System.out.println("Invalid ID token.");
        }

    }
}
