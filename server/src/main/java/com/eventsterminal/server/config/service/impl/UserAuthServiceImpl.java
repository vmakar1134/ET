package com.eventsterminal.server.config.service.impl;

import com.eventsterminal.server.config.model.Payload;
import com.eventsterminal.server.config.model.Role;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.eventsterminal.server.config.model.Roles.*;

@Service
public class UserAuthServiceImpl implements UserAuthService {

    private final UserAuthRepository userAuthRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenProviderServiceImpl tokenProviderService;

    private final AuthenticationManager authenticationManager;

    private final RoleRepository roleRepository;

    private final CustomUserDetailsService customUserDetailsService;

    public UserAuthServiceImpl(UserAuthRepository userAuthRepository, PasswordEncoder passwordEncoder,
                               TokenProviderServiceImpl tokenProviderService, AuthenticationManager authenticationManager,
                               RoleRepository roleRepository, CustomUserDetailsService customUserDetailsService) {
        this.userAuthRepository = userAuthRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProviderService = tokenProviderService;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public UserAuth register(AuthenticateRequest registerRequest) {
        if (userAuthRepository.existsByUsername(registerRequest.getLogin())) {
            throw new ConflictException("Login already registered");
        }
        return create(registerRequest);
    }

    private UserAuth create(AuthenticateRequest registerRequest) {
        UserAuth userAuth = new UserAuth();
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        userAuth.setUsername(registerRequest.getLogin());
        userAuth.setPassword(encodedPassword);
        userAuth.setRoles(getDefaultRole());
        return userAuthRepository.save(userAuth);
    }

    @Override
    public String login(HttpServletRequest servletRequest, AuthenticateRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = getLoginAuthenticationToken(servletRequest, loginRequest);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return tokenProviderService.generateToken(authenticationManager.authenticate(authenticationToken));
    }

    private UsernamePasswordAuthenticationToken getLoginAuthenticationToken(HttpServletRequest servletRequest, AuthenticateRequest loginRequest) {
        UserPrincipal userPrincipal = (UserPrincipal) customUserDetailsService.loadUserByUsername(loginRequest.getLogin());
        UsernamePasswordAuthenticationToken loginAuthenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal, loginRequest.getPassword(), userPrincipal.getAuthorities());
        loginAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(servletRequest));
        return loginAuthenticationToken;
    }

    @Override
    public String socialAuthorize(HttpServletRequest servletRequest, UserAuth userAuth) {
        PreAuthenticatedAuthenticationToken authenticationToken = getSocialAuthenticationToken(servletRequest, userAuth);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return tokenProviderService.generateToken(authenticationToken);
    }

    private PreAuthenticatedAuthenticationToken getSocialAuthenticationToken(HttpServletRequest request, UserAuth userAuth) {
        UserPrincipal userPrincipal = UserPrincipal.valueOf(userAuth);
        PreAuthenticatedAuthenticationToken socialAuthenticationToken = new PreAuthenticatedAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        socialAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return socialAuthenticationToken;
    }

    @Override
    public UserAuth fromPayload(Payload payload) {
        BigInteger socialId = new BigInteger(payload.getUserId());
        Optional<UserAuth> userAuthOpt = userAuthRepository.findBySocialId(socialId);
        return userAuthOpt.orElseGet(() -> create(payload));
    }

    private UserAuth create(Payload payload) {
        UserAuth userAuth = new UserAuth();
        List<Role> defaultRole = getDefaultRole();
        userAuth.setRoles(defaultRole);
        userAuth.setUsername(payload.getEmail());
        userAuth.setSocialId(new BigInteger(payload.getUserId()));
        return userAuthRepository.save(userAuth);
    }

    private List<Role> getDefaultRole() {
        return Collections.singletonList(roleRepository.findByName(ROLE_USER).orElse(new Role(ROLE_USER)));
    }

    @Override
    public UserAuth getCurrentUser() {
        UserAuth principal = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userAuthRepository.findById(principal.getId())
                .orElseThrow(() -> new NotFoundException("No user in security context"));
    }

    @Override
    public UserAuth findByUsername(String email) {
        return userAuthRepository.findByUsername(email)
                .orElseThrow(() -> new NotFoundException("UserAuth not found by email"));
    }

}
