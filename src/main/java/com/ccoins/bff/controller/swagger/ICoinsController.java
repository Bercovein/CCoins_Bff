package com.ccoins.bff.controller.swagger;

import com.ccoins.bff.dto.GenericRsDTO;
import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.LongDTO;
import com.ccoins.bff.dto.coins.CoinsReportDTO;
import com.ccoins.bff.dto.coins.CoinsReportStatesDTO;
import com.ccoins.bff.dto.coins.StateDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ccoins.bff.controller.swagger.SwaggerConstants.COINS;

@Api(tags = COINS)
public interface ICoinsController {
    @ApiOperation("Get coins report by filters")
    ResponseEntity<CoinsReportDTO> getCoinsReportByTableId(@RequestParam(value = "type", required = false) String type,
                                                           @RequestHeader HttpHeaders headers,
                                                           @PageableDefault(page = 0, size = 10, sort = "date") Pageable pagination);

    @ApiOperation("Get coins report by filters and table id")
    ResponseEntity<CoinsReportDTO> getCoinsReportByTableId(@RequestParam(value = "type", required = false) String type,
                                                           @RequestBody IdDTO tableId,
                                                           @PageableDefault(page = 0, size = 10, sort = "date") Pageable pagination);

    @ApiOperation("Get active states")
    ResponseEntity<List<StateDTO>> getActiveStates();

    @ApiOperation("Deliver prizes or coins by coins id")

    ResponseEntity<GenericRsDTO<Long>> deliverPrizeOrCoins(@RequestBody IdDTO id);

    @ApiOperation("Cancel prizes or coins by coins id")
    ResponseEntity<GenericRsDTO<Long>> cancelPrizeOrCoins(@RequestBody IdDTO id);

    @ApiOperation("Revert operation by coins id")
    ResponseEntity<GenericRsDTO<Long>> adjustPrizeOrCoins(@RequestBody IdDTO id);

    @ApiOperation("Get coins report in demand")
    ResponseEntity<GenericRsDTO<List<CoinsReportStatesDTO>>> getInDemandReport();

    @ApiOperation("Get coins that are already claimed")
    ResponseEntity<GenericRsDTO<List<CoinsReportStatesDTO>>> getNotDemandedReport();

    @ApiOperation("Counts in demand report")
    ResponseEntity<LongDTO> countInDemandReport();
}
