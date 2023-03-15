package com.ccoins.bff.controller.swagger;

import com.ccoins.bff.annotation.LimitedTime;
import com.ccoins.bff.dto.GenericRsDTO;
import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.ResponseDTO;
import io.swagger.annotations.Api;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Api(tags = "SONG VOTES")
public interface ISongVotesController {
    void voteSong(@RequestHeader HttpHeaders headers, @RequestBody IdDTO request);

    @GetMapping("/check-vote")
    @LimitedTime
    ResponseEntity<GenericRsDTO<ResponseDTO>> checkVote(@RequestHeader HttpHeaders headers);
}
