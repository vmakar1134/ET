package com.eventsterminal.server.config;

import com.eventsterminal.server.config.handler.CustomAuthenticationSuccessHandler;
import com.eventsterminal.server.config.service.impl.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@PropertySource("application-oauth2.properties")
public class AuthConfig extends WebSecurityConfigurerAdapter {

    // TODO: 7/22/20 from config (review)
    private static final List<String> CLIENTS = Arrays.asList("google", "facebook");

    private static final String CLIENT_PROPERTY_KEY = "spring.security.oauth2.client.registration.";

    private final Environment env;

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    private final CustomUserDetailsService customUserDetailsService;

    private final RequestProcessingJWTFilter requestProcessingJWTFilter;

    public AuthConfig(Environment env, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler, CustomUserDetailsService customUserDetailsService, RequestProcessingJWTFilter requestProcessingJWTFilter) {
        this.env = env;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.customUserDetailsService = customUserDetailsService;
        this.requestProcessingJWTFilter = requestProcessingJWTFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // TODO: 7/21/20 review endpoints
        http
                .authorizeRequests()
                // TODO: 7/22/20 to config
                .antMatchers("/login", "/auth/login/fail")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                // TODO: 7/22/20 to client
                .loginPage("/login")
                .authorizationEndpoint()
                // TODO: 7/22/20 set baseUri to config
                .baseUri("/auth/client")
                .authorizationRequestRepository(authorizationRequestRepository())
                .and()
                .tokenEndpoint()
                .accessTokenResponseClient(accessTokenResponseClient())
                .and()
                .successHandler(customAuthenticationSuccessHandler)
                // TODO: 7/22/20 add handler
                .failureUrl("/auth/login/fail")
                .and()
                .logout()
                // TODO: 7/22/20 POST request (review)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .and()
                .csrf()
                .disable();

        http.addFilterBefore(requestProcessingJWTFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        return new DefaultAuthorizationCodeTokenResponseClient();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = CLIENTS.stream()
                .map(this::getRegistration)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return new InMemoryClientRegistrationRepository(registrations);
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService() {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
    }

    // TODO: 7/21/20 refactor null response
    private ClientRegistration getRegistration(String client) {
        // TODO: 7/21/20 refactor string
        String clientId = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-id");
        if (clientId == null) {
            return null;
        }
        String clientSecret = env.getProperty(
                CLIENT_PROPERTY_KEY + client + ".client-secret");

        // TODO: 7/21/20 refactor if conditions
        if (client.equals("google")) {
            return CommonOAuth2Provider.GOOGLE.getBuilder(client)
                    .clientId(clientId).clientSecret(clientSecret).build();
        }
        if (client.equals("facebook")) {
            return CommonOAuth2Provider.FACEBOOK.getBuilder(client)
                    .clientId(clientId).clientSecret(clientSecret).build();
        }
        return null;
    }

}
