package com.ccoins.bff.service;

import com.ccoins.bff.dto.users.ClientDTO;
import com.ccoins.bff.dto.users.OwnerDTO;
import org.springframework.http.HttpHeaders;

import java.util.Optional;

public interface IUserService {

    OwnerDTO newOwner(String email);

    Optional<OwnerDTO> findByEmail(String email);

    OwnerDTO findByToken(HttpHeaders headers);

    Long getOwnerId(HttpHeaders headers);

    OwnerDTO findOrCreateOwner(String email);

    ClientDTO findOrCreateClient(ClientDTO request);

    Optional<ClientDTO> findActiveById(Long id);

    ClientDTO newClient(ClientDTO request);

    void loginClient(ClientDTO request);
}
