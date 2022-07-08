package com.ccoins.bff.service.impl;

import com.ccoins.bff.dto.users.ClientDTO;
import com.ccoins.bff.dto.users.ClientTableDTO;
import com.ccoins.bff.service.ILoginService;
import com.ccoins.bff.service.IPartiesService;
import com.ccoins.bff.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginService implements ILoginService {

    private final IPartiesService partyService;
    private final IUserService usersService;

    @Autowired
    public LoginService(IPartiesService partyService, IUserService usersService) {
        this.partyService = partyService;
        this.usersService = usersService;
    }


    @Override
    public void loginClient(ClientTableDTO request) {

        ClientDTO clientDTO = this.usersService.findActiveById(request.getClientId());

        this.partyService.asignOrCreatePartyByCode(request.getTableCode(), clientDTO);

    }


    @Override
    public void registerClient(ClientTableDTO request) {

        //dar de alta el cliente
        ClientDTO clientDTO = ClientDTO.builder().nickName(request.getName()).build();

        this.usersService.newClient(clientDTO);

        //Prepara la request para loguear al usuario
        ClientTableDTO loginRequest = ClientTableDTO.builder().clientId(clientDTO.getId()).tableCode(request.getTableCode()).build();

        //loguea al usuario
        this.loginClient(loginRequest);
    }
}