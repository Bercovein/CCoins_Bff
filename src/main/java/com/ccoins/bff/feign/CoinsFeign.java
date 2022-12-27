package com.ccoins.bff.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "${feign.coins-ms.name}", url = "${feign.coins-ms.url}")
@RequestMapping("${feign.coins-ms.req-map}")
public interface CoinsFeign {

    @GetMapping("/coins/party/{id}")
    ResponseEntity<Long> countCoinsByParty(@PathVariable("id") Long id);
}
