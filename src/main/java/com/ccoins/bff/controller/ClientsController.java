package com.ccoins.bff.controller;

import com.ccoins.bff.controller.swagger.IClientsController;
import com.ccoins.bff.dto.users.ClientTableDTO;
import com.ccoins.bff.service.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tables")
@CrossOrigin
public class ClientsController implements IClientsController {

    @Autowired
    private ILoginService service;

    public void login(ClientTableDTO request){
        this.service.loginClient(request);
    }
}
