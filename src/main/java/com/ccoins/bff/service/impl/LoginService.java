package com.ccoins.bff.service.impl;

import com.ccoins.bff.dto.users.ClientDTO;
import com.ccoins.bff.dto.users.ClientTableDTO;
import com.ccoins.bff.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class LoginService implements ILoginService{

    private final IPartiesService partyService;
    private final IUserService usersService;

    @Autowired
    public LoginService(IPartiesService partyService, IUserService usersService) {
        this.partyService = partyService;
        this.usersService = usersService;
    }


    @Override
    public void loginClient(ClientTableDTO request) {

        Optional<ClientDTO> clientOpt = this.usersService.findActiveById(request.getClientId());

        if(clientOpt.isEmpty()){
            // retornar a front que pida el nombre
        }

        this.partyService.asignOrCreatePartyByCode(request.getTableCode(), clientOpt.get());

    }

    @Override
    public void registerClient(ClientDTO request){

    }
}
