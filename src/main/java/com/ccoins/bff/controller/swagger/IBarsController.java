package com.ccoins.bff.controller.swagger;

import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.LogoutPartyDTO;
import com.ccoins.bff.dto.bars.BarDTO;
import com.ccoins.bff.dto.bars.BarListDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import static com.ccoins.bff.controller.swagger.SwaggerConstants.*;

@Api(tags = BAR)
public interface IBarsController {

    @ApiOperation(value = SAVE_OR_UPDATE)
    ResponseEntity<BarDTO> saveOrUpdate(@RequestBody BarDTO barDTO);

    @ApiOperation(value = FIND_BY_ID)
    ResponseEntity<BarDTO> findById(@RequestBody IdDTO id);

    @ApiOperation(value = FIND_ALL_BY + OWNER)
    ResponseEntity<BarListDTO> findAllByOwner();

    @ApiOperation(value = ACTIVATE_DEACTIVATE)
    ResponseEntity<BarDTO> active(@RequestBody IdDTO id);

    @ApiOperation(value = LOGOUT + "clients")
    void kickFromPartyByLeader(@RequestBody LogoutPartyDTO request);
}
