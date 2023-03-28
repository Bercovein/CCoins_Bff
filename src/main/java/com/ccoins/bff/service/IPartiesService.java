package com.ccoins.bff.service;

import com.ccoins.bff.dto.ListDTO;
import com.ccoins.bff.dto.LongDTO;
import com.ccoins.bff.dto.prizes.PartyDTO;
import com.ccoins.bff.dto.users.ClientDTO;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Optional;

public interface IPartiesService {

    Long asignOrCreatePartyByCode(String code, ClientDTO clientDTO);

    void asignClientToParty(Long partyId, Long clientId);

    PartyDTO createParty(Long tableId);

    Optional<PartyDTO> findActivePartyByTable(Long id);

    PartyDTO findActivePartyByTableCode(String code);

    LongDTO countCoinsByParty(Long id);

    PartyDTO findById(Long id);

    ListDTO findClientsFromParty(Long id, HttpHeaders headers);

    void logout(String client);

    List<ClientDTO> findByIdIn(List<Long> list);

    List<Long> findAllIdsByClients(List<Long> clientsIds);
}
