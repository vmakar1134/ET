package com.eventsterminal.server.config.service;

import javax.servlet.http.HttpServletRequest;

public interface GoogleAuthService {

    String authenticate(HttpServletRequest request);

}
