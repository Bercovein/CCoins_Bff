package com.ccoins.bff.controller.swagger;

import com.ccoins.bff.annotation.LimitedTime;
import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.ListDTO;
import com.ccoins.bff.dto.LongDTO;
import com.ccoins.bff.dto.prizes.PartyDTO;
import io.swagger.annotations.Api;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.ccoins.bff.controller.swagger.SwaggerConstants.PARTY;

@Api(tags = PARTY)
public interface IPartyController {

    @PostMapping("/coins/quantity")
    @LimitedTime
    ResponseEntity<LongDTO> getTotalCoinsByParty(@RequestBody IdDTO id);

    //info de la mesa (nombre de la party, nro de mesa..)
    @PostMapping
    ResponseEntity<PartyDTO> getPartyInfo(@RequestBody IdDTO id);

    //integrantes de la mesa
    @PostMapping("/clients")
    ResponseEntity<ListDTO> getClientsFromParty(@RequestBody IdDTO id, @RequestHeader HttpHeaders headers);

    @PostMapping("/buy")
    @LimitedTime
    void buyPrize(@RequestBody IdDTO idDTO, @RequestHeader HttpHeaders headers);

    @PostMapping({"/prizes"})
    ResponseEntity<ListDTO> findAllByBar(@RequestHeader HttpHeaders headers);
}
