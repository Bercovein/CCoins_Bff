package com.ccoins.bff.service;

import com.ccoins.bff.dto.*;
import com.ccoins.bff.dto.prizes.ClientPartyDTO;
import com.ccoins.bff.dto.prizes.PartyDTO;
import com.ccoins.bff.dto.users.ClientDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface IPartiesService {

    void asignOrCreatePartyByCode(ClientTableDTO request, ClientDTO clientDTO);

    ResponseEntity<ClientPartyDTO> asignClientToParty(Long partyId, Long clientId, boolean leader);

    PartyDTO createParty(Long tableId);

    Optional<PartyDTO> findActivePartyByTable(Long id);

    PartyDTO findActivePartyByTableCode(String code);

    LongDTO countCoinsByParty(Long id);

    PartyDTO findById(Long id);

    ListDTO findClientsFromPartyToClients(Long id, HttpHeaders headers);

    void logoutFromAnyParty(String client, Long partyId);

    void logoutFromAnyPartyBut(String client, Long partyId);

    List<ClientDTO> findByIdIn(List<Long> list);

    List<Long> findAllIdsByClients(List<Long> clientsIds);

    ResponseEntity<GenericRsDTO<ResponseDTO>> giveLeaderTo(HttpHeaders headers, IdDTO idDTO);

    ResponseEntity<GenericRsDTO<ResponseDTO>> kickFromPartyByLeader(LongListDTO request, HttpHeaders headers);

    ResponseEntity<GenericRsDTO<ResponseDTO>> kickFromPartyByOwner(LogoutPartyDTO request);

    ResponseEntity<GenericRsDTO<ResponseDTO>> kickFromParty(List<Long> list, Long partyId, boolean banned);

    boolean isBannedFromParty(ClientTableDTO request);

    ResponseEntity<List<PartyTableDTO>> findActivePartiesByOwner();

    ListDTO findClientsByPartyIdToOwner(IdDTO request);
}
