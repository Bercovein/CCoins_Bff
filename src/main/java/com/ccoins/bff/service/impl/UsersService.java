package com.ccoins.bff.service.impl;

import com.ccoins.bff.configuration.security.JwtUtils;
import com.ccoins.bff.dto.users.ClientDTO;
import com.ccoins.bff.dto.users.OwnerDTO;
import com.ccoins.bff.exceptions.BadRequestException;
import com.ccoins.bff.exceptions.ObjectNotFoundException;
import com.ccoins.bff.exceptions.constant.ExceptionConstant;
import com.ccoins.bff.feign.UsersFeign;
import com.ccoins.bff.service.IRandomNameService;
import com.ccoins.bff.service.IUserService;
import com.ccoins.bff.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UsersService implements IUserService {

    private final UsersFeign usersFeign;

    private final IRandomNameService randomize;

    @Autowired
    public UsersService(UsersFeign usersFeign, IRandomNameService randomize) {
        this.usersFeign = usersFeign;
        this.randomize = randomize;
    }

    @Override
    public OwnerDTO newOwner(String email) {

        OwnerDTO ownerDTO;

        try{
            ownerDTO = OwnerDTO.builder().email(email).startDate(DateUtils.nowLocalDateTime()).build();
            return this.usersFeign.saveOwner(ownerDTO);
        }catch(Exception e){
            throw new BadRequestException(ExceptionConstant.USERS_NEW_OWNER_ERROR_CODE, this.getClass(), ExceptionConstant.USERS_NEW_OWNER_ERROR);
        }
    }

    @Override
    public Optional<OwnerDTO> findByEmail(String email) {

        try{
            log.error("Buscando usuario por email");
            return this.usersFeign.findByEmail(email);
        }catch(Exception e){
            log.error("Error al ir contra feign");
            throw new BadRequestException(ExceptionConstant.USERS_GET_OWNER_BY_EMAIL_ERROR_CODE, this.getClass(), ExceptionConstant.USERS_GET_OWNER_BY_EMAIL_ERROR);
        }
    }


    @Override
    public OwnerDTO findByToken(HttpHeaders headers){
        Optional<OwnerDTO> ownerDTO = this.findByEmail(JwtUtils.get(headers, JwtUtils.TOKEN_EMAIL));

        if (ownerDTO.isEmpty()){
            throw new ObjectNotFoundException(ExceptionConstant.USERS_GET_OWNER_BY_EMAIL_ERROR_CODE, this.getClass(), ExceptionConstant.USERS_GET_OWNER_BY_EMAIL_ERROR);
        }

        return ownerDTO.get();
    }

    @Override
    public Long getOwnerId(HttpHeaders headers){
        return this.findByToken(headers).getId();
    }

    @Override
    public OwnerDTO findOrCreateOwner(String email){

        Optional<OwnerDTO> ownerOpt = this.findByEmail(email);
        OwnerDTO ownerDTO;
        if(ownerOpt.isEmpty()){
            log.error("Nuevo usuario");
            ownerDTO = this.newOwner(email);
        }else{
            ownerDTO = ownerOpt.get();
        }
        return ownerDTO;
    }

    @Override
    public ClientDTO findOrCreateClient(ClientDTO request){

        ClientDTO response;

        try {
            response = this.findActiveByIp(request.getIp());
        }catch(ObjectNotFoundException e) {
            log.error("Nuevo cliente");
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

}
