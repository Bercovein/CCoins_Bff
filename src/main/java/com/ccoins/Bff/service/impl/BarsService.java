package com.ccoins.Bff.service.impl;

import com.ccoins.Bff.dto.IdDTO;
import com.ccoins.Bff.dto.ListDTO;
import com.ccoins.Bff.dto.bars.BarDTO;
import com.ccoins.Bff.exceptions.BadRequestException;
import com.ccoins.Bff.exceptions.UnauthorizedException;
import com.ccoins.Bff.exceptions.constant.ExceptionConstant;
import com.ccoins.Bff.exceptions.utils.ErrorUtils;
import com.ccoins.Bff.feign.BarsFeign;
import com.ccoins.Bff.service.IBarsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BarsService extends ContextService implements IBarsService {

    private final BarsFeign barsFeign;


    @Autowired
    public BarsService(BarsFeign barsFeign) {
        this.barsFeign = barsFeign;
    }

    @Override
    public ResponseEntity<BarDTO> saveOrUpdate(BarDTO barDTO) {

        Long ownerId = super.getLoggedUserId();

        try{
            barDTO.setOwner(ownerId);
            return this.barsFeign.saveOrUpdateBar(barDTO);
        }catch(Exception e){
            log.error(ErrorUtils.parseMethodError(this.getClass()));
            throw new BadRequestException(ExceptionConstant.BARS_CREATE_OR_UPDATE_ERROR_CODE, this.getClass(), ExceptionConstant.BARS_CREATE_OR_UPDATE_ERROR);
        }
    }

    @Override
    public ResponseEntity<BarDTO> findById(IdDTO id) {

        Long ownerId = super.getLoggedUserId();
        BarDTO bar;

        try{
            bar = this.barsFeign.findBarById(id.getId()).getBody();
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
    public ResponseEntity<ListDTO> findAllByOwner() {

        Long ownerId = super.getLoggedUserId();

        try{
            return this.barsFeign.findAllBarsByOwner(ownerId);
        }catch(Exception e){
            log.error(ErrorUtils.parseMethodError(this.getClass()));
            throw new BadRequestException(ExceptionConstant.BARS_FIND_BY_OWNER_ERROR_CODE, this.getClass(), ExceptionConstant.BARS_FIND_BY_OWNER_ERROR);
        }
    }

    @Override
    public ResponseEntity<BarDTO> active(IdDTO request) {
        try{
            return this.barsFeign.activeBar(request.getId());
        }catch(Exception e){
            log.error(ErrorUtils.parseMethodError(this.getClass()));
            throw new BadRequestException(ExceptionConstant.BARS_ACTIVE_ERROR_CODE,
                    this.getClass(), ExceptionConstant.BARS_ACTIVE_ERROR);
        }
    }
}
