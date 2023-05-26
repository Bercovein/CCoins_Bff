package com.ccoins.bff.feign;

import com.ccoins.bff.dto.GenericRsDTO;
import com.ccoins.bff.dto.ListDTO;
import com.ccoins.bff.dto.LongListDTO;
import com.ccoins.bff.dto.ResponseDTO;
import com.ccoins.bff.dto.prizes.ClientPartyDTO;
import com.ccoins.bff.dto.prizes.PartyDTO;
import com.ccoins.bff.dto.prizes.PrizeDTO;
import com.ccoins.bff.dto.ClientTableDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "${feign.prizes-ms.name}", url = "${feign.prizes-ms.url}")
@RequestMapping("${feign.prizes-ms.req-map}")
public interface PrizeFeign {

    @PostMapping("/prizes")
    ResponseEntity<PrizeDTO> saveOrUpdatePrize(@RequestBody PrizeDTO request);

    @GetMapping("/prizes/owner/{barId}")
    ResponseEntity<ListDTO> findAllPrizesByBar(@PathVariable("barId") Long id);

    @GetMapping("/prizes/owner/{barId}/{status}")
    ResponseEntity<ListDTO> findAllPrizesByBarAndStatus(@PathVariable("barId") Long id, @PathVariable("status") String status);

    @GetMapping("/prizes/{id}")
    ResponseEntity<PrizeDTO> findPrizeById(@PathVariable("id") Long id);

    @PutMapping("/prizes/{id}/active")
    ResponseEntity<PrizeDTO> activePrize(@PathVariable("id") Long id);

    @PutMapping("/prizes")
    ResponseEntity<GenericRsDTO> activePrizesByList(@RequestBody ListDTO request);

    @GetMapping("/parties/table/{id}")
    Optional<PartyDTO> findActivePartyByTable(@PathVariable("id")Long id);

    @PostMapping("/parties/table")
    PartyDTO createParty(PartyDTO partyDTO);

    @PostMapping({"/parties/client"})
    void asignClientToParty(@RequestBody ClientPartyDTO request);

    @GetMapping("/parties/{id}")
    Optional<PartyDTO> findById(@PathVariable("id")Long id);

    @GetMapping("/parties/{id}/clients")
    List<ClientPartyDTO> findClientsByPartyId(@PathVariable("id") Long id);

    @DeleteMapping("/parties/client/{client}")
    void logoutClientFromTables(@PathVariable ("client") String client);

    @GetMapping("/parties/table/code/{code}")
    Optional<PartyDTO> findActivePartyByTableCode(@PathVariable("code") String code);

    @PostMapping("/parties/clients")
    List<Long> findAllIdsByClients(@RequestBody LongListDTO list);

    @PutMapping("/parties/leader/{leaderId}/to/{clientId}")
    ResponseEntity<GenericRsDTO<ResponseDTO>> giveLeaderTo(@PathVariable("leaderId") Long leaderId, @PathVariable("clientId") Long clientId);

    @DeleteMapping("/parties/{partyId}/close-if-inactive")
    boolean closePartyIfHaveNoClients(@PathVariable("partyId") Long partyId);

    @GetMapping("/parties/leader/{leaderIp}/party/{partyId}")
    ResponseEntity<Boolean> isLeaderFromParty(@PathVariable("leaderIp")String leaderIp, Long partyId);

    @DeleteMapping("/parties/{partyId}/client/{clientId}")
    void banClientFromParty(@PathVariable("clientId") Long clientId,@PathVariable("partyId") Long partyId);

    @PostMapping("/parties/is-banned")
    ResponseEntity<Boolean> isBannedFromParty(@RequestBody ClientTableDTO request);

    @GetMapping("/parties/bar/{id}")
    ResponseEntity<List<PartyDTO>> findActivePartiesByBar(@PathVariable("id") Long id);
}
