package com.ccoins.Bff.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "${feign.users-ms.name}", url = "${feign.users-ms.url}")
@RequestMapping("${feign.users-ms.req-ma}")
public interface UsersFeign {
}
