package com.ccoins.Bff.configuration.security.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@AllArgsConstructor
@Builder
public class JwtAuthentication implements Authentication {

    private final Long id;
    private Boolean verified;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;
    private String principal;
    private String name;

    @Override
    public Object getCredentials() {

        return null;
    }

    @Override
    public Object getDetails() {

        return null;
    }

    @Override
    public boolean isAuthenticated() {

        return true;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {

        throw new UnsupportedOperationException("JWT authentication is always authenticated");
    }

}
