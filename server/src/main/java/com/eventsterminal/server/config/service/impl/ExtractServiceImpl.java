package com.eventsterminal.server.config.service.impl;

import com.eventsterminal.server.config.model.Role;
import com.eventsterminal.server.config.model.UserAuth;
import com.eventsterminal.server.config.service.ExtractService;
import com.eventsterminal.server.domain.model.UserProfile;
import com.eventsterminal.server.repository.RoleRepository;
import com.eventsterminal.server.repository.UserAuthRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExtractServiceImpl implements ExtractService {

    private final RoleRepository roleRepository;

    private final UserAuthRepository userAuthRepository;

    public ExtractServiceImpl(RoleRepository roleRepository, UserAuthRepository userAuthRepository) {
        this.roleRepository = roleRepository;
        this.userAuthRepository = userAuthRepository;
    }

    @Override
    @Transactional
    // TODO: 7/25/20 set token fields to config
    public void saveDataFromOauthSession(Authentication authentication) {
        DefaultOidcUser principal = (DefaultOidcUser) authentication.getPrincipal();
        Map<String, Object> userInfo = principal.getAttributes();
        BigInteger socialId = new BigInteger((String) userInfo.get("sub"));
        if (userAuthRepository.findBySocialId(socialId).isPresent()) {
            return;
        }
        UserAuth userAuth = new UserAuth();
        userAuth.setSocialId(socialId);
        userAuth.setEmail((String) userInfo.get("email"));
        userAuth.setRoles(extractRoles(principal));
        userAuth.setUserProfile(extractUserProfile(userInfo));
        userAuthRepository.save(userAuth);
    }

    private List<Role> extractRoles(DefaultOidcUser principal) {
        List<String> oauthRoles = getOauthRoles(principal);
        return roleRepository.findAll()
                .stream()
                .filter(cr -> oauthRoles.contains(cr.getName().toString().toUpperCase()))
                .collect(Collectors.toList());
    }

    private List<String> getOauthRoles(DefaultOidcUser principal) {
        return principal.getAuthorities()
                .stream()
                .filter(a -> a instanceof OidcUserAuthority)
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    private UserProfile extractUserProfile(Map<String, Object> userInfo) {
        UserProfile userProfile = new UserProfile();
        userProfile.setName((String) userInfo.get("name"));
        userProfile.setGivenName((String) userInfo.get("given_name"));
        userProfile.setFamilyName((String) userInfo.get("family_name"));
        userProfile.setLocale(new Locale((String) userInfo.get("locale")));
        userProfile.setPicture(getPictureUrl(userInfo));

        return userProfile;
    }

    private URL getPictureUrl(Map<String, Object> userInfo) {
        URL picture = null;
        try {
            picture = new URL((String) userInfo.get("url"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return picture;
    }

}
