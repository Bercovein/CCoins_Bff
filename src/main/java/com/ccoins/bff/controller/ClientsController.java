package com.ccoins.bff.controller;

import com.ccoins.bff.annotation.LimitedTime;
import com.ccoins.bff.controller.swagger.IClientsController;
import com.ccoins.bff.dto.StringDTO;
import com.ccoins.bff.dto.users.ClientDTO;
import com.ccoins.bff.dto.users.ClientTableDTO;
import com.ccoins.bff.service.IBarsService;
import com.ccoins.bff.service.IClientService;
import com.ccoins.bff.utils.HeaderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
@CrossOrigin
public class ClientsController implements IClientsController {

    @Autowired
    private IClientService service;

    @Autowired
    private IBarsService barsService;

    @PostMapping("/login")
//    @MobileCheck
    @LimitedTime
    public ClientTableDTO login(@RequestHeader HttpHeaders headers, Device device){

        ClientTableDTO request = ClientTableDTO.builder()
                .clientIp(HeaderUtils.getClient(headers))
                .tableCode(HeaderUtils.getCode(headers))
                .build();

        return this.service.loginClient(request);
    }

    @PutMapping("/name")
    @LimitedTime
    @ResponseStatus(HttpStatus.CREATED)
    public void changeName(@RequestBody ClientDTO request, @RequestHeader HttpHeaders headers){
        request.setIp(HeaderUtils.getClient(headers));
        this.service.changeName(request);
    }

    @GetMapping("/logout")
    public void logout(@RequestHeader HttpHeaders headers){
        this.service.logout(HeaderUtils.getClient(headers));
    }

    @GetMapping("/menu")
    @LimitedTime
    @Override
    public ResponseEntity<StringDTO> findUrlMenu(@RequestHeader HttpHeaders headers){
        return this.barsService.findUrlMenu(headers);
    }
}
