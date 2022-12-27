package com.ccoins.bff.controller.swagger;

import com.ccoins.bff.annotation.LimitedTime;
import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.ListDTO;
import com.ccoins.bff.dto.LongDTO;
import com.ccoins.bff.dto.prizes.PartyDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/buy")
    @LimitedTime
    void buyPrize(@RequestBody IdDTO idDTO, @RequestHeader HttpHeaders headers);

    @PostMapping({"/prizes"})
    ResponseEntity<ListDTO> findAllByBar(@RequestHeader HttpHeaders headers);
}
