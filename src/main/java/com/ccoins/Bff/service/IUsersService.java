package com.ccoins.Bff.service;

import com.ccoins.Bff.dto.users.OwnerDTO;

import java.util.Optional;

public interface IUsersService {

    OwnerDTO newOwner(String email);

    Optional<OwnerDTO> findByEmail(String email);

    OwnerDTO findOrCreateOwner(String email);
}
