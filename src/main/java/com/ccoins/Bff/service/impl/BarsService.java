package com.ccoins.Bff.service.impl;

import com.ccoins.Bff.dto.IdDTO;
import com.ccoins.Bff.dto.ListDTO;
import com.ccoins.Bff.dto.bars.BarDTO;
import com.ccoins.Bff.exceptions.BadRequestException;
import com.ccoins.Bff.exceptions.UnauthorizedException;
import com.ccoins.Bff.exceptions.constant.ExceptionConstant;
import com.ccoins.Bff.feign.BarsFeign;
import com.ccoins.Bff.service.IBarsService;
import com.ccoins.Bff.service.IUsersService;
import com.ccoins.Bff.utils.ErrorUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BarsService implements IBarsService {

    private final BarsFeign barsFeign;

    private final IUsersService usersService;

    @Autowired
    public BarsService(BarsFeign barsFeign, IUsersService usersService) {
        this.barsFeign = barsFeign;
        this.usersService = usersService;
    }

    @Override
    public ResponseEntity<BarDTO> saveOrUpdate(BarDTO barDTO, HttpHeaders headers) {

        Long ownerId = this.usersService.getOwnerId(headers);

        try{
            barDTO.setOwner(ownerId);
            return this.barsFeign.saveOrUpdate(barDTO);
        }catch(Exception e){
            log.error(ErrorUtils.parseMethodError(this.getClass()));
            throw new BadRequestException(ExceptionConstant.BARS_CREATE_OR_UPDATE_ERROR_CODE, this.getClass(), ExceptionConstant.BARS_CREATE_OR_UPDATE_ERROR);
        }
    }

    @Override
    public ResponseEntity<BarDTO> findById(IdDTO id, HttpHeaders headers) {

        Long ownerId = this.usersService.getOwnerId(headers);
        BarDTO bar;

        try{
            bar = this.barsFeign.findById(id.getId()).getBody();
        }catch(Exception e){
            log.error(ErrorUtils.parseMethodError(this.getClass()));
            throw new BadRequestException(ExceptionConstant.BARS_FIND_BY_ID_ERROR_CODE, this.getClass(), ExceptionConstant.BARS_FIND_BY_ID_ERROR);
        }

        if(bar != null && !bar.getOwner().equals(ownerId)){
            throw new UnauthorizedException(ExceptionConstant.BARS_UNAUTHORIZED_ERROR_CODE, this.getClass(), ExceptionConstant.BARS_UNAUTHORIZED_ERROR);
        }

        return ResponseEntity.ok(bar);
    }

    @Override
    public ResponseEntity<ListDTO> findAllByOwner(HttpHeaders headers) {

        Long ownerId = this.usersService.getOwnerId(headers);

        try{
            return this.barsFeign.findAllByOwner(ownerId);
        }catch(Exception e){
            log.error(ErrorUtils.parseMethodError(this.getClass()));
            throw new BadRequestException(ExceptionConstant.BARS_FIND_BY_OWNER_ERROR_CODE, this.getClass(), ExceptionConstant.BARS_FIND_BY_OWNER_ERROR);
        }
    }
}
