package com.ccoins.bff.feign;

import com.ccoins.bff.spotify.sto.TokenSPTF;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "${spotify.token.name}", url = "${spotify.token.url}")
@RequestMapping("${spotify.token.req-map}")
public interface SpotifyTokenFeign {

    @PostMapping(value = "${spotify.token.path}", produces = "application/json", consumes = "application/json")
    TokenSPTF getOrRefreshToken(@RequestParam("grant_type") String grantType,
                                @RequestParam("code") String code,
                                @RequestParam("refresh_token") String refreshToken,
                                @RequestParam("redirect_uri") String redirectUri,
                                @RequestHeader HttpHeaders headers
                       );
}
