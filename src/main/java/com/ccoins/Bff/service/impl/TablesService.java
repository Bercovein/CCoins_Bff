package com.ccoins.Bff.service.impl;

import com.ccoins.Bff.dto.IdDTO;
import com.ccoins.Bff.dto.ListDTO;
import com.ccoins.Bff.dto.bars.TableDTO;
import com.ccoins.Bff.exceptions.BadRequestException;
import com.ccoins.Bff.exceptions.constant.ExceptionConstant;
import com.ccoins.Bff.feign.BarsFeign;
import com.ccoins.Bff.service.ITablesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
    public ResponseEntity<TableDTO> findById(IdDTO request) {

        Long ownerId = super.getLoggedUserId();
        TableDTO table;

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
    public ResponseEntity<ListDTO> findAllByBar(IdDTO request) {

//        Long ownerId = super.getLoggedUserId();

        try{
            return this.barsFeign.findAllTablesByBar(request.getId());
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
}
