package com.ccoins.Bff.service.impl;

import com.ccoins.Bff.configuration.security.PrincipalUserFactory;
import com.ccoins.Bff.dto.users.OwnerDTO;
import com.ccoins.Bff.exceptions.BadRequestException;
import com.ccoins.Bff.exceptions.UnauthorizedException;
import com.ccoins.Bff.feign.UsersFeign;
import com.ccoins.Bff.service.IUsersService;
import com.ccoins.Bff.utils.DateUtils;
import com.ccoins.Bff.utils.ErrorUtils;
import com.ccoins.Bff.utils.constant.ExceptionConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UsersService implements IUsersService, UserDetailsService {

    @Autowired
    private UsersFeign usersFeign;

    @Autowired
	PasswordEncoder passwordEncoder;
    
    @Value("${secretPsw}") 
    String secretPsw;

    @Override
    public OwnerDTO newOwner(String email) {

        OwnerDTO ownerDTO;

        try{
            ownerDTO = OwnerDTO.builder().email(email).startDate(DateUtils.now()).build();
            OwnerDTO newOwner = this.usersFeign.saveOwner(ownerDTO);
            return newOwner;
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        try{
            OwnerDTO ownerDTO = this.usersFeign.findByEmail(email).get();

            return PrincipalUserFactory.build(ownerDTO, passwordEncoder.encode(secretPsw));
        }catch(Exception e){
            log.error(ErrorUtils.parseMethodError(this.getClass()));
            throw new UnauthorizedException(ExceptionConstant.USER_NOT_FOUND_ERROR_CODE, this.getClass(), ExceptionConstant.USER_NOT_FOUND_ERROR);
        }
    }
}
