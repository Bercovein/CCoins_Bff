package com.ccoins.bff.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
public class CredentialsSPTFConfig {

    @Value("${spotify.config.auth.client-id}")
    private String clientId;

    @Value("${spotify.config.auth.secret}")
    private String clientSecret;

    @Value("${spotify.config.auth.url}")
    private String authEndpoint;

    @Value("${spotify.config.auth.scopes}")
    private String[] scopes;

    @Value("${spotify.config.auth.scopes}")
    private String grantType;

    @Value("${spotify.config.auth-code}")
    private String authorizationCode;

    @Value("${spotify.config.auth.refresh-token}")
    private String refreshToken;

    @Value("${spotify.config.auth.redirect-uri}")
    private String redirectURI;


}
