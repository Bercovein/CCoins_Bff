package com.ccoins.bff.service.impl;

import com.ccoins.bff.dto.GenericRsDTO;
import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.ListDTO;
import com.ccoins.bff.dto.LongListDTO;
import com.ccoins.bff.dto.bars.BarTableDTO;
import com.ccoins.bff.dto.bars.TableDTO;
import com.ccoins.bff.dto.bars.TableQuantityDTO;
import com.ccoins.bff.exceptions.BadRequestException;
import com.ccoins.bff.exceptions.constant.ExceptionConstant;
import com.ccoins.bff.dto.ResponseDTO;
import com.ccoins.bff.feign.BarsFeign;
import com.ccoins.bff.service.ITablesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TablesService extends ContextService implements ITablesService {

    private final BarsFeign barsFeign;


    @Autowired
    public TablesService(BarsFeign barsFeign) {
        this.barsFeign = barsFeign;
    }

    @Override
    public ResponseEntity<TableDTO> saveOrUpdate(TableDTO request) {

        try{
            return this.barsFeign.saveOrUpdateTable(request);
        }catch(Exception e){
            throw new BadRequestException(ExceptionConstant.TABLE_CREATE_OR_UPDATE_ERROR_CODE,
                    this.getClass(), ExceptionConstant.TABLE_CREATE_OR_UPDATE_ERROR);
        }
    }

    @Override
    public ResponseEntity<BarTableDTO> findById(IdDTO request) {

        Long ownerId = super.getLoggedUserId();
        BarTableDTO table;

        try{
            table = this.barsFeign.findTableById(request.getId()).getBody();
        }catch(Exception e){
            throw new BadRequestException(ExceptionConstant.TABLES_FIND_BY_ID_ERROR_CODE, this.getClass(), ExceptionConstant.TABLES_FIND_BY_ID_ERROR);
        }

//        if(table != null && !table.getOwner().equals(ownerId)){
//            throw new UnauthorizedException(ExceptionConstant.BARS_UNAUTHORIZED_ERROR_CODE, this.getClass(), ExceptionConstant.BARS_UNAUTHORIZED_ERROR);
//        }

        return ResponseEntity.ok(table);
    }

    @Override
    public ResponseEntity<ListDTO> findAllByBar(IdDTO request, Optional<String> status) {

//        Long ownerId = super.getLoggedUserId();
        try{
            if(status.isPresent()) {
                return this.barsFeign.findAllTablesByBarAndOptStatus(request.getId(), status);
            } else {
                return this.barsFeign.findAllTablesByBar(request.getId());
            }

        }catch(Exception e){
            throw new BadRequestException(ExceptionConstant.TABLES_FIND_BY_BAR_ERROR_CODE,
                    this.getClass(), ExceptionConstant.TABLES_FIND_BY_BAR_ERROR);
        }
    }

    @Override
    public ResponseEntity<TableDTO> active(IdDTO request) {
        try{
            return this.barsFeign.activeTable(request.getId());
        }catch(Exception e){
            throw new BadRequestException(ExceptionConstant.TABLES_ACTIVE_ERROR_CODE,
                    this.getClass(), ExceptionConstant.TABLES_ACTIVE_ERROR);
        }
    }

    @Override
    public ResponseEntity<GenericRsDTO> createByQuantity(TableQuantityDTO request){
        return this.barsFeign.createByQuantity(request);
    }

    @Override
    public ResponseEntity<GenericRsDTO> deleteByQuantity(TableQuantityDTO request) {
        return this.barsFeign.deleteByQuantity(request);
    }

    @Override
    public ResponseEntity<GenericRsDTO> activeByList(LongListDTO request) {
        return this.barsFeign.activeByList(request);
    }

    @Override
    public ResponseEntity<ResponseDTO> generateCodesByList(LongListDTO request){

        return this.barsFeign.generateCodesByList(request);
    }

    @Override
    public List<BarTableDTO> findByIdIn(LongListDTO request) {

        return this.barsFeign.findByIdIn(request);
    }

    @Override
    public BarTableDTO findByCode(String code) {
        return this.barsFeign.findByCode(code);
    }
}
