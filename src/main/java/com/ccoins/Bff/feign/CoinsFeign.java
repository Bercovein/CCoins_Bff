package com.ccoins.Bff.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "${feign.coins-ms.name}", url = "${feign.coins-ms.url}")
@RequestMapping("demo")
public interface CoinsFeign {

    @GetMapping
    ResponseEntity<String> demo();
}
