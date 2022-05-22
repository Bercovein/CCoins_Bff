package com.ccoins.Bff.service.impl;

import com.ccoins.Bff.dto.users.OwnerDTO;
import com.ccoins.Bff.exceptions.BadRequestException;
import com.ccoins.Bff.exceptions.ObjectNotFoundException;
import com.ccoins.Bff.exceptions.constant.ExceptionConstant;
import com.ccoins.Bff.feign.UsersFeign;
import com.ccoins.Bff.service.IUsersService;
import com.ccoins.Bff.utils.DateUtils;
import com.ccoins.Bff.exceptions.utils.ErrorUtils;
import com.ccoins.Bff.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UsersService implements IUsersService  {

    private final UsersFeign usersFeign;

    @Autowired
    public UsersService(UsersFeign usersFeign) {
        this.usersFeign = usersFeign;
    }

    @Override
    public OwnerDTO newOwner(String email) {

        OwnerDTO ownerDTO;

        try{
            ownerDTO = OwnerDTO.builder().email(email).startDate(DateUtils.nowLocalDateTime()).build();
            return this.usersFeign.saveOwner(ownerDTO);
        }catch(Exception e){
            log.error(ErrorUtils.parseMethodError(this.getClass()));
            throw new BadRequestException(ExceptionConstant.USERS_NEW_OWNER_ERROR_CODE, this.getClass(), ExceptionConstant.USERS_NEW_OWNER_ERROR);
        }
    }

    @Override
    public Optional<OwnerDTO> findByEmail(String email) {

        try{
            return this.usersFeign.findByEmail(email);
        }catch(Exception e){
            log.error(ErrorUtils.parseMethodError(this.getClass()));
            throw new BadRequestException(ExceptionConstant.USERS_GET_OWNER_BY_EMAIL_ERROR_CODE, this.getClass(), ExceptionConstant.USERS_GET_OWNER_BY_EMAIL_ERROR);
        }
    }


    @Override
    public OwnerDTO findByToken(HttpHeaders headers){
        log.error(String.valueOf(headers));
        Optional<OwnerDTO> ownerDTO = this.findByEmail(TokenUtils.get(headers, TokenUtils.TOKEN_EMAIL));

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
            ownerDTO = this.newOwner(email);
        }else{
            ownerDTO = ownerOpt.get();
        }
        return ownerDTO;
    }


}
