package com.eventsterminal.server.config.service.impl;

import com.eventsterminal.server.config.model.UserPrincipal;
import com.eventsterminal.server.exception.NotFoundException;
import com.eventsterminal.server.repository.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAuthRepository userAuthRepository;

    @Autowired
    public CustomUserDetailsService(UserAuthRepository userAuthRepository) {
        this.userAuthRepository = userAuthRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userAuthRepository.findByUsername(s)
                .map(UserPrincipal::valueOf)
                .orElseThrow(() -> new NotFoundException("User not found by login"));
    }

    public UserDetails loadUserById(Long id) {
        return userAuthRepository.findById(id)
                .map(UserPrincipal::valueOf)
                .orElseThrow(() -> new NotFoundException("User not found by id"));
    }

}
