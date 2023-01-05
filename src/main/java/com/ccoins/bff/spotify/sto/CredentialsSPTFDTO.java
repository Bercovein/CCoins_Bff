package com.ccoins.bff.spotify.sto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CredentialsSPTFDTO {

    private String clientId;

    private String clientSecret;

    private String authEndpoint;

    private String[] scopes;
}
