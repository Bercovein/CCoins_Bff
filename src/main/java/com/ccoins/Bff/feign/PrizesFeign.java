package com.ccoins.Bff.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "${feign.prizes-ms.name}", url = "${feign.prizes-ms.url}")
@RequestMapping("${feign.prizes-ms.req-map}")
public interface PrizesFeign {
}
