package com.eventsterminal.server.config.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class ExtractServiceImpl implements ExtractService {

    private final List<String> USER_PRINCIPAL_FIELDS = Arrays.asList("name", "id", "email");

    // TODO: 7/22/20 extract and return UserPrincipal
    @Override
    public Object extractPrincipal(Authentication authentication) {
        Map<String, Object> userInfo = ((DefaultOidcUser) authentication.getPrincipal()).getAttributes();
        return userInfo.get(USER_PRINCIPAL_FIELDS.get(0));
    }

}
