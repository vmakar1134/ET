package com.eventsterminal.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class CustomAuthenticationSuccessHandler
        extends SavedRequestAwareAuthenticationSuccessHandler
        implements PrincipalExtractor {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        super.setDefaultTargetUrl("/home");
        super.onAuthenticationSuccess(request, response, authentication);
        // TODO: 7/22/20 save new user to database
        // TODO: 7/22/20 to service
        DefaultOidcUser principal = (DefaultOidcUser) authentication.getPrincipal();
        Map<String, Object> userInfo = principal.getAttributes();
        Object o = extractPrincipal(userInfo);


    }

    // TODO: 7/22/20 extract UserPrincipal
    // TODO: 7/22/20 to service
    @Override
    public Object extractPrincipal(Map<String, Object> map) {
        map.get("name");
        return map.get("name");
    }

}
