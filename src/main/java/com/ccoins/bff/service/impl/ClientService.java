package com.ccoins.bff.service.impl;

import com.ccoins.bff.dto.users.ClientDTO;
import com.ccoins.bff.dto.users.ClientTableDTO;
import com.ccoins.bff.exceptions.BadRequestException;
import com.ccoins.bff.exceptions.ObjectNotFoundException;
import com.ccoins.bff.exceptions.constant.ExceptionConstant;
import com.ccoins.bff.feign.UsersFeign;
import com.ccoins.bff.service.IClientService;
import com.ccoins.bff.service.IPartiesService;
import com.ccoins.bff.service.IRandomNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService implements IClientService {

    private final IPartiesService partyService;

    private final UsersFeign usersFeign;

    private final IRandomNameService randomize;

    @Autowired
    public ClientService(IPartiesService partyService, UsersFeign usersFeign, IRandomNameService randomize) {
        this.partyService = partyService;
        this.usersFeign = usersFeign;
        this.randomize = randomize;
    }


    @Override
    public ClientTableDTO loginClient(ClientTableDTO request) {

        ClientDTO clientDTO = ClientDTO.builder().ip(request.getClientIp()).build();

        clientDTO = this.findOrCreateClient(clientDTO);

        Long partyId = this.partyService.asignOrCreatePartyByCode(request.getTableCode(), clientDTO);

        return ClientTableDTO.builder().partyId(partyId).clientIp(clientDTO.getIp()).tableCode(request.getTableCode()).nickName(clientDTO.getNickName()).build();
    }

    @Override
    public ClientDTO findOrCreateClient(ClientDTO request){

        ClientDTO response;

        try {
            response = this.findActiveByIp(request.getIp());
        }catch(ObjectNotFoundException e) {
            request.setNickName(this.randomize.randomDefaultName());
            response = this.newClient(request);
        }
        return response;
    }

    @Override
    public ClientDTO findActiveByIp(String ip) {

        Optional<ClientDTO> clientOpt;

        try{
            clientOpt = this.usersFeign.findActiveByIp(ip);
        }catch(Exception e){
            throw new BadRequestException(ExceptionConstant.USERS_GET_CLIENT_ERROR_CODE,
                    this.getClass(), ExceptionConstant.USERS_GET_CLIENT_ERROR);
        }

        if(clientOpt.isEmpty()){
            throw new ObjectNotFoundException(ExceptionConstant.CLIENT_NOT_FOUND_ERROR_CODE,
                    this.getClass(), ExceptionConstant.CLIENT_NOT_FOUND_ERROR);
        }

        return clientOpt.get();
    }


    @Override
    public ClientDTO newClient(ClientDTO request) {
        try{
            return this.usersFeign.saveClient(request);
        }catch(Exception e){
            throw new BadRequestException(ExceptionConstant.USERS_NEW_CLIENT_ERROR_CODE,
                    this.getClass(), ExceptionConstant.USERS_NEW_CLIENT_ERROR);
        }
    }

    @Override
    public void changeName(ClientDTO request) {
        try{
            this.usersFeign.updateName(request);
        }catch(Exception e){
            throw new BadRequestException(ExceptionConstant.UPDATE_CLIENT_NAME_ERROR_CODE,
                    this.getClass(), ExceptionConstant.UPDATE_CLIENT_NAME_ERROR);
        }
    }

    @Override
    public void logout(String client) {

        this.partyService.logout(client);
    }
}