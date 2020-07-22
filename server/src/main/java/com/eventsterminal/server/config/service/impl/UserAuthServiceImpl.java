package com.eventsterminal.server.config.service.impl;

import com.eventsterminal.server.config.TokenProvider;
import com.eventsterminal.server.config.model.Role;
import com.eventsterminal.server.config.model.Roles;
import com.eventsterminal.server.config.model.UserAuth;
import com.eventsterminal.server.config.model.UserPrincipal;
import com.eventsterminal.server.config.service.UserAuthService;
import com.eventsterminal.server.domain.request.AuthenticateRequest;
import com.eventsterminal.server.exception.ConflictException;
import com.eventsterminal.server.exception.NotFoundException;
import com.eventsterminal.server.repository.RoleRepository;
import com.eventsterminal.server.repository.UserAuthRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserAuthServiceImpl implements UserAuthService {

    private final UserAuthRepository userAuthRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;

    private final RoleRepository roleRepository;

    public UserAuthServiceImpl(UserAuthRepository userAuthRepository, PasswordEncoder passwordEncoder,
                               TokenProvider tokenProvider, AuthenticationManager authenticationManager,
                               RoleRepository roleRepository) {
        this.userAuthRepository = userAuthRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
    }

    @Override
    public void register(AuthenticateRequest registerRequest) {
        if (userAuthRepository.existsByEmail(registerRequest.getLogin())) {
            throw new ConflictException("Login already registered");
        }

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        UserAuth userAuth = new UserAuth(encodedPassword, registerRequest.getLogin());

        Role role = roleRepository.findByName(Roles.ROLE_USER)
                .orElseThrow(() -> new NotFoundException("Role not found"));
        userAuth.setRoles(Collections.singletonList(role));
        userAuthRepository.save(userAuth);
    }

    @Override
    public String login(AuthenticateRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getLogin(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        return tokenProvider.generateToken(authenticate);
    }

    @Override
    public UserAuth getCurrentUser() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userAuthRepository.findById(principal.getId())
                .orElseThrow(() -> new NotFoundException("No user in security context"));
    }

    @Override
    public UserAuth findByEmail(String email) {
        return userAuthRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("UserAuth not found by email"));
    }

}
