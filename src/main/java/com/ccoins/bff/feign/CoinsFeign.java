package com.ccoins.bff.feign;

import com.ccoins.bff.dto.coins.VotingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "${feign.coins-ms.name}", url = "${feign.coins-ms.url}")
@RequestMapping("${feign.coins-ms.req-map}")
public interface CoinsFeign {

    @GetMapping("/coins/party/{id}")
    ResponseEntity<Long> countCoinsByParty(@PathVariable("id") Long id);

    @GetMapping("/match/voting/bar/{id}")
    ResponseEntity<VotingDTO> getActualVotingByBar(@PathVariable("id") Long id);

    @PostMapping("/match/voting")
    VotingDTO saveOrUpdateVoting(@RequestBody VotingDTO request);
}
