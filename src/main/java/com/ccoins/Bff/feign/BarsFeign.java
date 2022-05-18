package com.ccoins.Bff.feign;

import com.ccoins.Bff.dto.bars.BarDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "${feign.bars-ms.name}", url = "${feign.bars-ms.url}")
@RequestMapping("${feign.bars-ms.req-map}")
public interface BarsFeign {
    @PostMapping
    ResponseEntity<BarDTO> saveOrUpdate(@RequestBody BarDTO barDTO);
}
