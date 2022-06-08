package com.ccoins.Bff.controller.swagger;

import com.ccoins.Bff.dto.IdDTO;
import com.ccoins.Bff.dto.ListDTO;
import com.ccoins.Bff.dto.bars.GameDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import static com.ccoins.Bff.controller.swagger.SwaggerConstants.*;

@Api(tags = GAME)
public interface IGamesController {

    @ApiOperation(value = SAVE_OR_UPDATE)
    ResponseEntity<GameDTO> saveOrUpdate(@RequestBody GameDTO request);

    @ApiOperation(value = FIND_BY_ID)
    ResponseEntity<GameDTO> findById(@RequestBody IdDTO id);

    @ApiOperation(value = FIND_ALL_BY + GAME)
    ResponseEntity<ListDTO> findAllByBar(Long id);


    @ApiOperation(value = ACTIVATE_DEACTIVATE)
    ResponseEntity<GameDTO> active(@RequestBody IdDTO id);
}
