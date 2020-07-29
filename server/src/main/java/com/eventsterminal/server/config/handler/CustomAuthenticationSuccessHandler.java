package com.eventsterminal.server.config.handler;

import com.eventsterminal.server.config.service.ExtractService;
import com.eventsterminal.server.repository.UserAuthRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final ExtractService extractService;

    public CustomAuthenticationSuccessHandler(ExtractService extractService, UserAuthRepository userAuthRepository) {
        this.extractService = extractService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        extractService.saveDataFromOauthSession(authentication);
        response.setHeader("Access-Control-Allow-Origin", "*");
        super.setDefaultTargetUrl("/home");
        super.onAuthenticationSuccess(request, response, authentication);
    }

}
