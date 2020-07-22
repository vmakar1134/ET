package com.eventsterminal.server.config.service;

import org.springframework.security.core.Authentication;

public interface ExtractService {

    // TODO: 7/22/20 extract and return UserPrincipal
    Object extractPrincipal(Authentication authentication);

}
