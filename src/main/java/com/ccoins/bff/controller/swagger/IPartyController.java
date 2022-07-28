package com.ccoins.bff.controller.swagger;

import com.ccoins.bff.dto.LongDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;

import static com.ccoins.bff.controller.swagger.SwaggerConstants.*;

@Api(tags = PARTY)
public interface IPartyController {

    @ApiOperation(value = COUNT + "coins by party")
    ResponseEntity<LongDTO> getTotalCoinsByParty(Long id);
}
