package com.ccoins.bff.feign;

import com.ccoins.bff.spotify.sto.TokenSPTF;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;


@FeignClient(name = "${spotify.token.name}", url = "${spotify.token.url}", configuration = SpotifyTokenFeign.Configuration.class)
@RequestMapping("${spotify.token.req-map}")
public interface SpotifyTokenFeign {

    @PostMapping(value = "${spotify.token.path}", consumes = APPLICATION_FORM_URLENCODED_VALUE)
    TokenSPTF getOrRefreshToken(@RequestHeader HttpHeaders headers,
                                @RequestBody Map<String, ?> form);

    class Configuration {
        @Bean
        Encoder feignFormEncoder(ObjectFactory<HttpMessageConverters> converters) {
            return new SpringFormEncoder(new SpringEncoder(converters));
        }
    }
}
