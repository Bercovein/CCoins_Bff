package com.ccoins.Bff.controller.swagger;

import com.ccoins.Bff.dto.IdDTO;
import com.ccoins.Bff.dto.ListDTO;
import com.ccoins.Bff.dto.prizes.PrizeDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

import static com.ccoins.Bff.controller.swagger.SwaggerConstants.*;

@Api(tags = PRIZE)
public interface IPrizesController {

    @ApiOperation(value = SAVE_OR_UPDATE)
    ResponseEntity<PrizeDTO> saveOrUpdate(@RequestBody PrizeDTO request);

    @ApiOperation(value = FIND_BY_ID)
    ResponseEntity<PrizeDTO> findById(@RequestBody IdDTO id);

    @ApiOperation(value = FIND_ALL_BY + PRIZE + AND_OR_STATUS)
    ResponseEntity<ListDTO> findAllByBar(@RequestBody IdDTO request, @PathVariable("status") Optional<String> status);

    @ApiOperation(value = ACTIVATE_DEACTIVATE)
    ResponseEntity<PrizeDTO> active(@RequestBody IdDTO id);
}
