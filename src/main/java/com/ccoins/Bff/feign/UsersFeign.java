package com.ccoins.Bff.feign;

import com.ccoins.Bff.dto.users.OwnerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient(name = "${feign.users-ms.name}", url = "${feign.users-ms.url}")
@RequestMapping("${feign.users-ms.req-map}")
public interface UsersFeign {

    @PostMapping("/owner")
    OwnerDTO saveOwner(@RequestBody OwnerDTO owner);

    @GetMapping("/owner/email/{email}")
    Optional<OwnerDTO> findByEmail(@PathVariable("email") String email);
}