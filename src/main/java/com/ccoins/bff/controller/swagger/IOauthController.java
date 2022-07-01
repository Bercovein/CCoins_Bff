package com.ccoins.bff.controller.swagger;

import com.ccoins.bff.dto.TokenDTO;
import com.ccoins.bff.exceptions.CustomException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import static com.ccoins.bff.controller.swagger.SwaggerConstants.*;

@Api(tags = LOGIN)
public interface IOauthController {

    @ApiOperation(value = LOGIN_REGISTER_WITH + GOOGLE)
    ResponseEntity<?> google(@RequestBody TokenDTO request) throws CustomException;

    @ApiOperation(value = LOGIN_REGISTER_WITH + FACEBOOK)
    ResponseEntity<?> facebook(@RequestBody TokenDTO request) throws CustomException;
}
