package com.ccoins.Bff.service;

import com.ccoins.Bff.dto.users.request.OwnerDTO;

import java.util.Optional;

public interface IUsersService {

    void newOwner(String email);

    Optional<OwnerDTO> findByEmail(String email);

    void findOrCreateOwner(String email);
}
