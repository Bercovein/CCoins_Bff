package com.ccoins.bff.controller.swagger;

import com.ccoins.bff.dto.StringDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.ccoins.bff.controller.swagger.SwaggerConstants.CLIENT;
import static com.ccoins.bff.controller.swagger.SwaggerConstants.FIND_BY;

@Api(tags = CLIENT)
public interface IClientsController {

    @ApiOperation(value = FIND_BY + " url menu")
    ResponseEntity<StringDTO> findUrlMenu(@RequestHeader HttpHeaders headers);
}
