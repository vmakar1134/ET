package com.eventsterminal.server.config.service;


import com.eventsterminal.server.config.model.UserAuth;
import com.eventsterminal.server.domain.request.AuthenticateRequest;

public interface UserAuthService {

    void register(AuthenticateRequest registerRequest);

    String login(AuthenticateRequest loginRequest);

    UserAuth getCurrentUser();

    UserAuth findByEmail(String email);

}
