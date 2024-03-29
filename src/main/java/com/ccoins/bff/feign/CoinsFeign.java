package com.ccoins.bff.feign;

import com.ccoins.bff.dto.GenericRsDTO;
import com.ccoins.bff.dto.LongDTO;
import com.ccoins.bff.dto.ResponseDTO;
import com.ccoins.bff.dto.StringDTO;
import com.ccoins.bff.dto.coins.*;
import com.ccoins.bff.spotify.sto.CodeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "${feign.coins-ms.name}", url = "${feign.coins-ms.url}")
@RequestMapping("${feign.coins-ms.req-map}")
public interface CoinsFeign {

    @GetMapping("/coins/party/{id}/count")
    ResponseEntity<Long> countCoinsByParty(@PathVariable("id") Long id);

    @GetMapping("/match/voting/bar/{id}")
    ResponseEntity<VotingDTO> getActualVotingByBar(@PathVariable("id") Long id);

    @PostMapping("/match/voting")
    VotingDTO saveVoting(@RequestBody VotingDTO request);

    @PutMapping("/match/voting")
    VotingDTO updateVoting(@RequestBody VotingDTO request);

    @GetMapping("/match/voting/song/{songId}")
    VotingDTO getVotingBySong(@PathVariable("songId") Long songId);

    @PostMapping("/vote")
    void voteSong(@RequestBody VoteDTO request);

    @GetMapping("/vote/clients/song/{songId}")
    ResponseEntity<List<Long>> getClientsIdWhoVotedSong(@PathVariable("songId") Long songId);

    @PostMapping("/coins/clients/match")
    ResponseEntity<List<Long>> giveCoinsToClients(@RequestBody CoinsToWinnersDTO request);

    @PostMapping("/coins/party/prize/buy")
    ResponseEntity<ResponseDTO> spendCoinsInPrizeByParty(@RequestBody SpendCoinsRqDTO request);

    @GetMapping("/coins/party/{id}")
    ResponseEntity<CoinsReportDTO> getAllCoinsFromParty(@PathVariable("id") Long id,
                                                        @PageableDefault Pageable pagination,
                                                        @RequestParam(value = "type", required = false) String type);

    @GetMapping("/vote/client/{userIp}/bar/{barId}")
    boolean hasVotedAlready(@PathVariable("userIp") String userIp, @PathVariable("barId")  Long barId);

    @PutMapping("/vote/expire/{maxVotingTime}")
    void closeVotingByTime(@PathVariable("maxVotingTime") Integer maxVotingTime);

    @PutMapping("/vote/close/bar/{id}")
    void closeVotingByBarId(@PathVariable("id") Long barId);

    @GetMapping("/coins/states")
    ResponseEntity<List<StateDTO>> getAllStates();

    @GetMapping("/coins/active-states")
    ResponseEntity<List<StateDTO>> getActiveStates();

    @PostMapping("/coins/{id}/deliver")
    ResponseEntity<GenericRsDTO<Long>> deliverPrizeOrCoins(@PathVariable("id") Long id);

    @PostMapping("/coins/{id}/cancel")
    ResponseEntity<GenericRsDTO<Long>> cancelPrizeOrCoins(@PathVariable("id") Long id);

    @PostMapping("/coins/{id}/adjust")
    ResponseEntity<GenericRsDTO<Long>> adjustPrizeOrCoins(@PathVariable("id") Long id);

    @GetMapping("/coins/bar/{id}/ended-coins")
    ResponseEntity<GenericRsDTO<List<CoinsReportStatesDTO>>> getNotDemandedReport(@PathVariable("id") Long id);

    @GetMapping("/coins/bar/{id}/in-demand")
    ResponseEntity<GenericRsDTO<List<CoinsReportStatesDTO>>> getInDemandReport(@PathVariable("id") Long id);

    @GetMapping("/coins/bar/{id}/count-demand")
    ResponseEntity<LongDTO> countInDemandReport(@PathVariable("id") Long id);

    @PostMapping("/codes")
    ResponseEntity<List<CodeDTO>> createCodeByGameBarId(@RequestBody @Valid CodeRqDTO request);

    @PutMapping("/codes/invalidate")
    ResponseEntity<CodeDTO> invalidateCode(@RequestBody @Valid StringDTO request);

    @GetMapping("/codes/game/{id}/{state}")
    ResponseEntity<List<CodeDTO>> getByActive(@PathVariable("id") Long id, @PathVariable("state") String state);

    @PostMapping("/codes/redeem")
    ResponseEntity<GenericRsDTO<CoinsDTO>> redeemCode(@RequestBody @Valid RedeemCodeRqDTO request);
}
