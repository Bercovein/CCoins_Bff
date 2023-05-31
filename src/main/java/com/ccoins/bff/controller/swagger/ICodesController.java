package com.ccoins.bff.controller.swagger;

import com.ccoins.bff.dto.GenericRsDTO;
import com.ccoins.bff.dto.StringDTO;
import com.ccoins.bff.dto.coins.CodeRqDTO;
import com.ccoins.bff.dto.coins.CoinsDTO;
import com.ccoins.bff.dto.coins.RedeemCodeRqDTO;
import com.ccoins.bff.spotify.sto.CodeDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.ccoins.bff.controller.swagger.SwaggerConstants.CODE;

@Api(tags = CODE)
public interface ICodesController {

    @ApiOperation("Create new code from bar")
    ResponseEntity<List<CodeDTO>> createCodeByGameBarId(@RequestBody @Valid CodeRqDTO request);

    @ApiOperation("Invalidate code")
    ResponseEntity<CodeDTO> invalidateCode(@RequestBody StringDTO request);

    @ApiOperation("Get codes by state (active/inactive)")
    ResponseEntity<List<CodeDTO>> getByActive(@PathVariable("state") String state);

    @ApiOperation("Redeem code to a concrete client from party")
    ResponseEntity<GenericRsDTO<CoinsDTO>> redeemCode(@RequestBody @Valid RedeemCodeRqDTO request);
}
