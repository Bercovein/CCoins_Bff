package com.ccoins.bff.feign;

import com.ccoins.bff.dto.users.ClientDTO;
import com.ccoins.bff.dto.users.OwnerDTO;
import com.ccoins.bff.dto.users.RefreshTokenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "${feign.users-ms.name}", url = "${feign.users-ms.url}")
@RequestMapping("${feign.users-ms.req-map}")
public interface UsersFeign {

    @PostMapping("/owner")
    OwnerDTO saveOwner(@RequestBody OwnerDTO owner);

    @GetMapping("/owner/email/{email}")
    Optional<OwnerDTO> findByEmail(@PathVariable("email") String email);

    @GetMapping("/owner/{id}")
    ResponseEntity<RefreshTokenDTO> getSpotifyRefreshTokenByOwnerId(@PathVariable("id") Long id);

    @PutMapping("/owner/{id}")
    void saveOrUpdateRefreshTokenSpotify(@PathVariable("id") Long id, @RequestBody RefreshTokenDTO request);

    @PostMapping("/client")
    ClientDTO saveClient(ClientDTO request);

    @GetMapping("/client/ip/{ip}")
    Optional<ClientDTO> findActiveByIp(@PathVariable("ip") String ip);

    @PutMapping("/client/name")
    void updateName(ClientDTO request);

    @PostMapping("/client/list")
    List<ClientDTO> findByIdIn(@RequestBody List<Long> list);

    @GetMapping("/client/party/{partyId}")
    ResponseEntity<List<ClientDTO>> findByParty(@PathVariable("partyId") Long partyId);


}
