package com.ccoins.Bff.service;

import com.ccoins.Bff.dto.users.OwnerDTO;
import org.springframework.http.HttpHeaders;

import java.util.Optional;

public interface IUserService {

    OwnerDTO newOwner(String email);

    Optional<OwnerDTO> findByEmail(String email);

    OwnerDTO findByToken(HttpHeaders headers);

    Long getOwnerId(HttpHeaders headers);

    OwnerDTO findOrCreateOwner(String email);
}
