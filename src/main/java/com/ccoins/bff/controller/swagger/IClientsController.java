package com.ccoins.bff.controller.swagger;

import com.ccoins.bff.annotation.LimitedTime;
import com.ccoins.bff.dto.StringDTO;
import io.swagger.annotations.Api;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.ccoins.bff.controller.swagger.SwaggerConstants.CLIENT;

@Api(tags = CLIENT)
public interface IClientsController {

    @GetMapping("/menu")
    @LimitedTime
    ResponseEntity<StringDTO> findUrlMenu(@RequestHeader HttpHeaders headers);
}
