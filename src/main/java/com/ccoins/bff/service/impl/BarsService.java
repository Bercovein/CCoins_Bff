package com.ccoins.bff.service.impl;

import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.StringDTO;
import com.ccoins.bff.dto.bars.BarDTO;
import com.ccoins.bff.dto.bars.BarListDTO;
import com.ccoins.bff.exceptions.BadRequestException;
import com.ccoins.bff.exceptions.UnauthorizedException;
import com.ccoins.bff.exceptions.constant.ExceptionConstant;
import com.ccoins.bff.feign.BarsFeign;
import com.ccoins.bff.service.IBarsService;
import com.ccoins.bff.utils.HeaderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BarsService extends ContextService implements IBarsService {

    @Autowired
    public BarsService(BarsFeign barsFeign) {
        super(barsFeign);
    }

    @Override
    public ResponseEntity<BarDTO> saveOrUpdate(BarDTO barDTO) {

        Long ownerId = super.getLoggedUserId();
        barDTO.setOwner(ownerId);

        ResponseEntity<BarListDTO> barResponse = null;

        if(barDTO.getId() == null) {
            try {
                barResponse = this.barsFeign.findAllBarsByOwner(ownerId);
            } catch (Exception e) {
                throw new BadRequestException(ExceptionConstant.BARS_CREATE_OR_UPDATE_ERROR_CODE, this.getClass(), ExceptionConstant.BARS_CREATE_OR_UPDATE_ERROR);
            }
        }

        if(barResponse != null && barResponse.hasBody()){

            BarListDTO barList = barResponse.getBody();

            if (barList != null && barList.getList() != null && !barList.getList().isEmpty()) {
                throw new BadRequestException(ExceptionConstant.BARS_ALREADY_CREATED_ERROR_CODE,
                        this.getClass(),
                        ExceptionConstant.BARS_ALREADY_CREATED_ERROR);
            }
        }

        try{
            return this.barsFeign.saveOrUpdateBar(barDTO);
        }catch(Exception e){
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
            throw new BadRequestException(ExceptionConstant.BARS_FIND_BY_ID_ERROR_CODE, this.getClass(), ExceptionConstant.BARS_FIND_BY_ID_ERROR);
        }

        if(bar != null && !bar.getOwner().equals(ownerId)){
            throw new UnauthorizedException(ExceptionConstant.BARS_UNAUTHORIZED_ERROR_CODE, this.getClass(), ExceptionConstant.BARS_UNAUTHORIZED_ERROR);
        }


        return ResponseEntity.ok(bar);
    }

    @Override
    public ResponseEntity<BarListDTO> findAllByOwner() {

        Long ownerId = super.getLoggedUserId();

        try{
            return this.barsFeign.findAllBarsByOwner(ownerId);
        }catch(Exception e){
            throw new BadRequestException(ExceptionConstant.BARS_FIND_BY_OWNER_ERROR_CODE, this.getClass(), ExceptionConstant.BARS_FIND_BY_OWNER_ERROR);
        }
    }

    @Override
    public ResponseEntity<BarDTO> active(IdDTO request) {
        try{
            return this.barsFeign.activeBar(request.getId());
        }catch(Exception e){
            throw new BadRequestException(ExceptionConstant.BARS_ACTIVE_ERROR_CODE,
                    this.getClass(), ExceptionConstant.BARS_ACTIVE_ERROR);
        }
    }

    @Override
    public ResponseEntity<StringDTO> findUrlMenu(HttpHeaders headers) {

        try {
            String code = HeaderUtils.getCode(headers);
            return this.barsFeign.findUrlByTableCode(code);
        }catch (Exception e){
            throw new BadRequestException(ExceptionConstant.URL_MENU_ERROR_CODE,
                    this.getClass(), ExceptionConstant.URL_MENU_ERROR);
        }
    }

}
