package com.ccoins.Bff.service.impl;

import com.ccoins.Bff.dto.IdDTO;
import com.ccoins.Bff.dto.ListDTO;
import com.ccoins.Bff.dto.bars.BarDTO;
import com.ccoins.Bff.exceptions.BadRequestException;
import com.ccoins.Bff.feign.BarsFeign;
import com.ccoins.Bff.service.IBarsService;
import com.ccoins.Bff.utils.ErrorUtils;
import com.ccoins.Bff.utils.constant.ExceptionConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BarsService implements IBarsService {

    @Autowired
    private BarsFeign barsFeign;

    @Override
    public ResponseEntity<BarDTO> saveOrUpdate(BarDTO barDTO, HttpHeaders headers) {

        try{
            //METER LO QUE VENGA POR HEADERS EN BARDTO
            return this.barsFeign.saveOrUpdate(barDTO);
        }catch(Exception e){
            log.error(ErrorUtils.parseMethodError(this.getClass()));
            throw new BadRequestException(ExceptionConstant.BARS_CREATE_OR_UPDATE_ERROR_CODE, this.getClass(), ExceptionConstant.BARS_CREATE_OR_UPDATE_ERROR);
        }
    }

    @Override
    public ResponseEntity<BarDTO> findById(IdDTO id, HttpHeaders headers) {

        try{
            return null;
        }catch(Exception e){
            log.error(ErrorUtils.parseMethodError(this.getClass()));
            throw new BadRequestException(ExceptionConstant.BARS_FIND_BY_ID_ERROR_CODE, this.getClass(), ExceptionConstant.BARS_FIND_BY_ID_ERROR);
        }
    }

    @Override
    public ResponseEntity<ListDTO> findAllByOwner(HttpHeaders headers) {

        try{
            return null;
        }catch(Exception e){
            log.error(ErrorUtils.parseMethodError(this.getClass()));
            throw new BadRequestException(ExceptionConstant.BARS_FIND_BY_OWNER_ERROR_CODE, this.getClass(), ExceptionConstant.BARS_FIND_BY_OWNER_ERROR);
        }
    }
}
