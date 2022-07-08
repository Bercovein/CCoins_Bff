package com.ccoins.bff.service;

import com.ccoins.bff.dto.users.ClientDTO;

public interface IPartiesService {

    Long asignOrCreatePartyByCode(String code, ClientDTO clientDTO);
}
