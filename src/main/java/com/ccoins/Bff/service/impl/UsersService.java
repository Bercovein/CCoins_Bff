package com.ccoins.Bff.service.impl;

import com.ccoins.Bff.dto.users.request.OwnerDTO;
import com.ccoins.Bff.exceptions.BadRequestException;
import com.ccoins.Bff.feign.UsersFeign;
import com.ccoins.Bff.service.IUsersService;
import com.ccoins.Bff.utils.DateUtils;
import com.ccoins.Bff.utils.ErrorUtils;
import com.ccoins.Bff.utils.constant.ExceptionConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UsersService implements IUsersService {

    @Autowired
    private UsersFeign usersFeign;

    @Override
    public void newOwner(String email) {

        OwnerDTO ownerDTO;

        try{
            ownerDTO = OwnerDTO.builder().email(email).startDate(DateUtils.now()).build();
            this.usersFeign.saveOwner(ownerDTO);
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
    public void findOrCreateOwner(String email){

        Optional<OwnerDTO> ownerOpt = this.findByEmail(email);

        if(ownerOpt.isEmpty()){
            this.newOwner(email);
        }
    }
}
