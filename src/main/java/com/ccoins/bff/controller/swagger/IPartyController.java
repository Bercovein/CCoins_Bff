package com.ccoins.bff.controller.swagger;

import com.ccoins.bff.dto.*;
import com.ccoins.bff.dto.prizes.PartyDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.ccoins.bff.controller.swagger.SwaggerConstants.PARTY;

@Api(tags = PARTY)
public interface IPartyController {

    @ApiOperation("Find total coins by party id")
    ResponseEntity<LongDTO> getTotalCoinsByParty(@RequestBody IdDTO id);

    //info de la mesa (nombre de la party, nro de mesa..)
    @ApiOperation("Find party info by id")
    ResponseEntity<PartyDTO> getPartyInfo(@RequestBody IdDTO id);

    //integrantes de la mesa
    @ApiOperation("Find clients by party id and client ip")
    ResponseEntity<ListDTO> getClientsFromParty(@RequestBody IdDTO id, @RequestHeader HttpHeaders headers);

    @ApiOperation("Buy a prize by id, table code and client ip")
    ResponseEntity<ResponseDTO> buyPrize(@RequestBody IdDTO idDTO, @RequestHeader HttpHeaders headers);

    @ApiOperation("Find all parties by table code in header")
    ResponseEntity<ListDTO> findAllByBar(@RequestHeader HttpHeaders headers);

    @ApiOperation("Find all games by table code in header")
    ResponseEntity<ListDTO> findGamesByBar(@RequestHeader HttpHeaders headers);

    @ApiOperation("Gives leader to another player")
    ResponseEntity<GenericRsDTO<ResponseDTO>> giveLeaderTo(@RequestBody IdDTO idDTO, @RequestHeader HttpHeaders headers);
}
