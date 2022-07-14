package com.ccoins.bff.controller;

import com.ccoins.bff.controller.swagger.IClientsController;
import com.ccoins.bff.dto.users.ClientTableDTO;
import com.ccoins.bff.service.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/clients")
@CrossOrigin
public class ClientsController implements IClientsController {

    @Autowired
    private ILoginService service;

    @PostMapping("/login")
    public ClientTableDTO login(@RequestBody @Valid ClientTableDTO request){
        return this.service.loginClient(request);
    }
}
