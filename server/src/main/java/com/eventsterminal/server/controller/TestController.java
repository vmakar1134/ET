package com.eventsterminal.server.controller;

import com.eventsterminal.server.config.model.UserAuth;
import com.eventsterminal.server.repository.UserAuthRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    private final UserAuthRepository userAuthRepository;

    public TestController(UserAuthRepository userAuthRepository) {
        this.userAuthRepository = userAuthRepository;
    }

    @GetMapping("users")

    public List<UserAuth> getTest() {
        return userAuthRepository.findAll();
    }

}
