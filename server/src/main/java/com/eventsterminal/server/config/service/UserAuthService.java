package com.eventsterminal.server.config.service;


import com.eventsterminal.server.config.model.Payload;
import com.eventsterminal.server.config.model.UserAuth;
import com.eventsterminal.server.config.model.UserPrincipal;
import com.eventsterminal.server.domain.request.AuthenticateRequest;

import javax.servlet.http.HttpServletRequest;

public interface UserAuthService {

    UserAuth register(AuthenticateRequest registerRequest);

    String login(HttpServletRequest request, AuthenticateRequest loginRequest);

    String socialAuthorize(HttpServletRequest request, UserAuth userAuth);

    UserAuth fromPayload(Payload payload);

    UserAuth getCurrentUser();

    UserAuth findByUsername(String email);

}
