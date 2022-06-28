package com.ccoins.Bff.feign;

import com.ccoins.Bff.dto.GenericRsDTO;
import com.ccoins.Bff.dto.ListDTO;
import com.ccoins.Bff.dto.prizes.PrizeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient(name = "${feign.prizes-ms.name}", url = "${feign.prizes-ms.url}")
@RequestMapping("${feign.prizes-ms.req-map}")
public interface PrizeFeign {

    @PostMapping
    ResponseEntity<PrizeDTO> saveOrUpdatePrize(@RequestBody PrizeDTO request);

    @GetMapping("/prizes/owner/{barId}")
    ResponseEntity<ListDTO> findAllPrizesByBar(@PathVariable("barId") Long id);

    @GetMapping("/prizes/owner/{barId}/{status}")
    ResponseEntity<ListDTO> findAllPrizesByBarAndStatus(@PathVariable("barId") Long id, @PathVariable("status") Optional<String> status);

    @GetMapping("/prizes/{id}")
    ResponseEntity<PrizeDTO> findPrizeById(@PathVariable("id") Long id);

    @PutMapping("/prizes/{id}/active")
    ResponseEntity<PrizeDTO> activePrize(@PathVariable("id") Long id);

    @PutMapping("/prizes")
    ResponseEntity<GenericRsDTO> activePrizesByList(@RequestBody ListDTO request);
}
