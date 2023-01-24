package com.ccoins.bff.controller.swagger;

import com.ccoins.bff.dto.IdDTO;
import io.swagger.annotations.Api;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Api(tags = "SONG VOTES")
public interface ISongVotesController {
    void voteSong(@RequestHeader HttpHeaders headers, @RequestBody IdDTO request);
}
