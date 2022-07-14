package com.ccoins.bff.feign;

import com.ccoins.bff.dto.users.ClientDTO;
import com.ccoins.bff.dto.users.OwnerDTO;
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

    @PostMapping("/client")
    ClientDTO saveClient(ClientDTO request);

    @GetMapping("/client/ip/{id}")
    Optional<ClientDTO> findActiveByIp(@PathVariable("id")String id);
}
