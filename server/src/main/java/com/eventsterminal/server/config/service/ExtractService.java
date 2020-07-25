package com.eventsterminal.server.config.service;

import org.springframework.security.core.Authentication;

public interface ExtractService {

    void saveDataFromOauthSession(Authentication authentication);

}
