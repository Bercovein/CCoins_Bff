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
    public ClientTableDTO loginClient(ClientTableDTO request) {

        ClientDTO clientDTO = ClientDTO.builder().id(request.getClientId()).build();

        clientDTO = this.usersService.findOrCreateClient(clientDTO);

        this.partyService.asignOrCreatePartyByCode(request.getTableCode(), clientDTO);

        return ClientTableDTO.builder().clientId(clientDTO.getId()).tableCode(request.getTableCode()).name(clientDTO.getNickName()).build();
    }

}