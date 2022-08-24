package com.ccoins.bff.controller.swagger;

import com.ccoins.bff.dto.ListDTO;
import com.ccoins.bff.dto.LongDTO;
import com.ccoins.bff.dto.prizes.PartyDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.ccoins.bff.controller.swagger.SwaggerConstants.*;

@Api(tags = PARTY)
public interface IPartyController {

    @ApiOperation(value = COUNT + "coins by party")
    ResponseEntity<LongDTO> getTotalCoinsByParty(Long id);

    //info de la mesa (nombre de la party, nro de mesa..)
    @GetMapping("/{id}")
    ResponseEntity<PartyDTO> getPartyInfo(@PathVariable("id") Long id);

    //integrantes de la mesa
    @GetMapping("/{id}/clients")
    ResponseEntity<ListDTO> getClientsFromParty(@PathVariable("id") Long id, @RequestHeader HttpHeaders headers);
}
