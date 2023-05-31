package com.ccoins.bff.controller.swagger;

import com.ccoins.bff.dto.GenericRsDTO;
import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.ResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Api(tags = "SONG VOTES")
public interface ISongVotesController {

    @ApiOperation("Vote song by id, party id and client ip")
    void voteSong(@RequestHeader HttpHeaders headers, @RequestBody IdDTO request);

    @ApiOperation("Checks if client already has voted by ip and party id")
    ResponseEntity<GenericRsDTO<ResponseDTO>> checkVote(@RequestHeader HttpHeaders headers);
}
