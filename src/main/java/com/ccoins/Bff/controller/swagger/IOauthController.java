package com.ccoins.Bff.controller.swagger;

import com.ccoins.Bff.dto.TokenDTO;
import com.ccoins.Bff.exceptions.CustomException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import static com.ccoins.Bff.controller.swagger.SwaggerConstants.*;

@Api(tags = LOGIN)
public interface IOauthController {

    @ApiOperation(value = LOGIN_REGISTER_WITH + GOOGLE,
            notes = LOGIN_REGISTER_WITH + GOOGLE)
    ResponseEntity<?> google(@RequestBody TokenDTO request) throws CustomException;

    @ApiOperation(value = LOGIN_REGISTER_WITH + FACEBOOK,
            notes = LOGIN_REGISTER_WITH + FACEBOOK)
    ResponseEntity<?> facebook(@RequestBody TokenDTO request) throws CustomException;
}
