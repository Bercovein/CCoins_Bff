package com.ccoins.bff.controller.swagger;

import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.PartyClientsDTO;
import com.ccoins.bff.dto.PartyTableDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static com.ccoins.bff.controller.swagger.SwaggerConstants.PARTY;
import static com.ccoins.bff.controller.swagger.SwaggerConstants.TABLE;

@Api(tags = PARTY + " " + TABLE)
public interface IPartyTablesController {


    @ApiOperation("Find active parties by owner logged")
    ResponseEntity<List<PartyTableDTO>> findActivePartiesByOwner();

    @ApiOperation("Find clients from party id")
    ResponseEntity<PartyClientsDTO> findClientsByPartyIdToOwner(@RequestBody IdDTO request);
}
