package com.ccoins.bff.controller;

import com.ccoins.bff.controller.swagger.IClientsController;
import com.ccoins.bff.dto.users.ClientDTO;
import com.ccoins.bff.dto.users.ClientTableDTO;
import com.ccoins.bff.service.IClientService;
import com.ccoins.bff.utils.HeaderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/clients")
@CrossOrigin
public class ClientsController implements IClientsController {

    @Autowired
    private IClientService service;

    @PostMapping("/login")
    public ClientTableDTO login(@RequestBody @Valid ClientTableDTO request){
        return this.service.loginClient(request);
    }

    @PutMapping("/name")
    @ResponseStatus(HttpStatus.CREATED)
    public void changeName(@RequestBody ClientDTO request, @RequestHeader HttpHeaders headers){
        request.setIp(HeaderUtils.getClient(headers));
        this.service.changeName(request);
    }
}
