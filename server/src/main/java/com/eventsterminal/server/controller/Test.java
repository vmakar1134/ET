package com.eventsterminal.server.controller;

import com.eventsterminal.server.config.dto.Oauth2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class Test {

    @Autowired
    private Oauth2 oauth2;

    @GetMapping("test")
    public Oauth2 test() {
        return oauth2;
    }

}
