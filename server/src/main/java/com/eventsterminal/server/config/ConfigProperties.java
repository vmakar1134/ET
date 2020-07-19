package com.eventsterminal.server.config;

import com.eventsterminal.server.config.dto.Oauth2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigProperties {

    @Bean
    @ConfigurationProperties(prefix = "oauth2")
    public Oauth2 oauth2() {
        return new Oauth2();
    }

}
