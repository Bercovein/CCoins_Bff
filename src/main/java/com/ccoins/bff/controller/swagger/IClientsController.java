package com.ccoins.bff.controller.swagger;

import com.ccoins.bff.dto.ClientTableDTO;
import com.ccoins.bff.dto.StringDTO;
import com.ccoins.bff.dto.users.ClientDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.ccoins.bff.controller.swagger.SwaggerConstants.CLIENT;
import static com.ccoins.bff.controller.swagger.SwaggerConstants.FIND_BY;

@Api(tags = CLIENT)
public interface IClientsController {

    @ApiOperation(value = "Login client only from device")
    ClientTableDTO login(@RequestHeader HttpHeaders headers, Device device);

    @ApiOperation(value = "Change name of client")
    void changeName(@RequestBody ClientDTO request, @RequestHeader HttpHeaders headers);

    @ApiOperation(value = "Logout client from system")
    void logout(@RequestHeader HttpHeaders headers);

    @ApiOperation(value = FIND_BY + " url menu")
    ResponseEntity<StringDTO> findUrlMenu(@RequestHeader HttpHeaders headers);
}
