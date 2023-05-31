package com.ccoins.bff.controller.swagger;

import com.ccoins.bff.dto.GenericRsDTO;
import com.ccoins.bff.dto.StringDTO;
import com.ccoins.bff.dto.coins.CoinsDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.ccoins.bff.controller.swagger.SwaggerConstants.REDEEMS;

@Api(tags = REDEEMS)
public interface IRedeemsController {

    @ApiOperation("Redeems code by client ip")
    ResponseEntity<GenericRsDTO<CoinsDTO>> redeemCode(@RequestBody StringDTO request, @RequestHeader HttpHeaders headers);
}
