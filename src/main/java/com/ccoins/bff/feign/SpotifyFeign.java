package com.ccoins.bff.feign;

import com.ccoins.bff.modal.PlaylistSPTF;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "${feign.spotify.name}", url = "${feign.spotify.url}")
@RequestMapping("${feign.spotify.req-map}")
public interface SpotifyFeign {

    @GetMapping("/queue")
    ResponseEntity<PlaylistSPTF> getQueue(@RequestHeader HttpHeaders headers);
}
