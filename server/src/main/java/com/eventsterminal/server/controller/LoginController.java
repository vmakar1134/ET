package com.eventsterminal.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

// TODO: 7/21/20 RestController
@Controller
public class LoginController {

    // TODO: 7/21/20 to application-custom-properties
    private static String authorizationRequestBaseUri = "oauth2/authorize-client";

    Map<String, String> oauth2AuthenticationUrls = new HashMap<>();

    // TODO: 7/21/20 to constructor
    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    // TODO: 7/21/20 to constructor
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/auth-login")
    public String getLoginPage(Model model) {
        // TODO: 7/21/20 to service (probably to filter)
        // TODO: 7/22/20 to rest
        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository).as(Iterable.class);
        if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }
        clientRegistrations.forEach(registration -> oauth2AuthenticationUrls.put(registration.getClientName(), authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
        model.addAttribute("urls", oauth2AuthenticationUrls);
        return "login";
    }

    @GetMapping("/login_success")
    public String getLoginInfo(Model model, OAuth2AuthenticationToken authentication) {
        // TODO: 7/22/20 move logic to CustomAuthtenticationSuccessHandlerService
        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName());
        String userInfoEndpointUri = client.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUri();

        if (!StringUtils.isEmpty(userInfoEndpointUri)) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken()
                    .getTokenValue());
            HttpEntity entity = new HttpEntity("", headers);
            ResponseEntity<Map> response = restTemplate
                    .exchange(userInfoEndpointUri, HttpMethod.GET, entity, Map.class);
            Map userAttributes = response.getBody();
            model.addAttribute("name", userAttributes.get("name"));
        }
        return "login_success";
    }

    // TODO: 7/21/20 remove, for test only
    @GetMapping("/home")
    public String home(OAuth2AuthenticationToken authenticationToken) {
        return "login_success";
    }

}

