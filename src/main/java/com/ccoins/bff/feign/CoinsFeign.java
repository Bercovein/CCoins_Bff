package com.ccoins.bff.feign;

import com.ccoins.bff.dto.ResponseDTO;
import com.ccoins.bff.dto.coins.CoinsToWinnersDTO;
import com.ccoins.bff.dto.coins.SpendCoinsRqDTO;
import com.ccoins.bff.dto.coins.VoteDTO;
import com.ccoins.bff.dto.coins.VotingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "${feign.coins-ms.name}", url = "${feign.coins-ms.url}")
@RequestMapping("${feign.coins-ms.req-map}")
public interface CoinsFeign {

    @GetMapping("/coins/party/{id}")
    ResponseEntity<Long> countCoinsByParty(@PathVariable("id") Long id);

    @GetMapping("/match/voting/bar/{id}")
    ResponseEntity<VotingDTO> getActualVotingByBar(@PathVariable("id") Long id);

    @PostMapping("/match/voting")
    VotingDTO saveOrUpdateVoting(@RequestBody VotingDTO request);

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
}
