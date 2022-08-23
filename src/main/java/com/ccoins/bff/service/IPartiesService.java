package com.ccoins.bff.service;

import com.ccoins.bff.dto.LongDTO;
import com.ccoins.bff.dto.prizes.PartyDTO;
import com.ccoins.bff.dto.users.ClientDTO;

import java.util.Optional;

public interface IPartiesService {

    Long asignOrCreatePartyByCode(String code, ClientDTO clientDTO);

    void asignClientToParty(Long partyId, Long clientId);

    PartyDTO createParty(Long tableId);

    Optional<PartyDTO> findActivePartyByTable(Long id);

    LongDTO countCoinsByParty(Long id);

    PartyDTO findById(Long id);
}
