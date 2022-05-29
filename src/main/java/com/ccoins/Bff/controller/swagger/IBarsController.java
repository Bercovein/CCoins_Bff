package com.ccoins.Bff.controller.swagger;

import com.ccoins.Bff.dto.IdDTO;
import com.ccoins.Bff.dto.ListDTO;
import com.ccoins.Bff.dto.bars.BarDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import static com.ccoins.Bff.controller.swagger.SwaggerConstants.*;

@Api(tags = BAR)
public interface IBarsController {

    @ApiOperation(value = SAVE_OR_UPDATE,
            notes = SAVE_OR_UPDATE_NOTE)
    ResponseEntity<BarDTO> saveOrUpdate(@RequestBody BarDTO barDTO);

    @ApiOperation(value = FIND_BY_ID,
            notes= FIND_BY_ID_NOTE)
    ResponseEntity<BarDTO> findById(@RequestBody IdDTO id);

    @ApiOperation(value = FIND_ALL_BY + OWNER,
            notes= FIND_ALL_BY_NOTE + OWNER)
    ResponseEntity<ListDTO> findAllByOwner();


    @ApiOperation(value = ACTIVATE_DEACTIVATE,
            notes= ACTIVATE_DEACTIVATE_NOTE)
    ResponseEntity<BarDTO> active(@RequestBody IdDTO id);
}
