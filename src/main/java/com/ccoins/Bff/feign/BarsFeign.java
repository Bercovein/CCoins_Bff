package com.ccoins.Bff.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "${feign.bars-ms.name}", url = "${feign.bars-ms.url}")
@RequestMapping("${feign.bars-ms.req-map}")
public interface BarsFeign {
}
