package com.ccoins.bff.controller.swagger;

import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.ListDTO;
import com.ccoins.bff.dto.prizes.PrizeDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Optional;

import static com.ccoins.bff.controller.swagger.SwaggerConstants.*;

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

    @PostMapping("/buy")
    void buyPrize(@RequestBody IdDTO idDTO, @RequestHeader HttpHeaders headers);
}
