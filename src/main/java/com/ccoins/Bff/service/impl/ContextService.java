package com.ccoins.Bff.service.impl;

import com.ccoins.Bff.configuration.security.authentication.JwtAuthentication;
import com.ccoins.Bff.service.IContextService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ContextService implements IContextService {

    @Override
    public Long getLoggedUserId() {
        return ((JwtAuthentication) SecurityContextHolder.getContext().getAuthentication()).getId();
    }
}
