package com.ccoins.bff.service.impl;

import com.ccoins.bff.dto.users.ClientDTO;
import com.ccoins.bff.dto.ClientTableDTO;
import com.ccoins.bff.exceptions.BadRequestException;
import com.ccoins.bff.exceptions.ObjectNotFoundException;
import com.ccoins.bff.exceptions.UnauthorizedException;
import com.ccoins.bff.exceptions.constant.ExceptionConstant;
import com.ccoins.bff.feign.UsersFeign;
import com.ccoins.bff.service.IClientService;
import com.ccoins.bff.service.IPartiesService;
import com.ccoins.bff.service.IRandomNameService;
import com.ccoins.bff.service.IServerSentEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.ccoins.bff.exceptions.constant.ExceptionConstant.CLIENT_BANNED_FROM_PARTY_ERROR;
import static com.ccoins.bff.exceptions.constant.ExceptionConstant.CLIENT_BANNED_FROM_PARTY_ERROR_CODE;
import static com.ccoins.bff.utils.enums.EventNamesEnum.NEW_CLIENT_TO_PARTY;

@Service
public class ClientService implements IClientService {

    private final IPartiesService partyService;

    private final UsersFeign usersFeign;

    private final IServerSentEventService sseService;

    private final IRandomNameService randomize;

    @Autowired
    public ClientService(IPartiesService partyService, UsersFeign usersFeign, IServerSentEventService sseService, IRandomNameService randomize) {
        this.partyService = partyService;
        this.usersFeign = usersFeign;
        this.sseService = sseService;
        this.randomize = randomize;
    }


    @Override
    public ClientTableDTO loginClient(ClientTableDTO request) {

        ClientDTO clientDTO = ClientDTO.builder().ip(request.getClientIp()).build();

        clientDTO = this.findOrCreateClient(clientDTO);

        boolean isBanned = this.partyService.isBannedFromParty(request);

        if(isBanned){
            throw new UnauthorizedException(CLIENT_BANNED_FROM_PARTY_ERROR_CODE, this.getClass(),CLIENT_BANNED_FROM_PARTY_ERROR);
        }

        this.partyService.asignOrCreatePartyByCode(request, clientDTO);

        request.setClientId(clientDTO.getId());
        request.setNickName(clientDTO.getNickName());

        Long partyId = request.getPartyId();

        if (partyId != null){
            this.sseService.dispatchEventToClientsFromParty(NEW_CLIENT_TO_PARTY.name(),String.format(NEW_CLIENT_TO_PARTY.getMessage(),clientDTO.getNickName()),partyId);
        }

        return request;
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
    public void logout(String client, Long partyId) {
        this.partyService.logout(client, partyId);
    }
}