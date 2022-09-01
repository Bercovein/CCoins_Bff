package com.ccoins.bff.service;

import com.ccoins.bff.dto.GenericRsDTO;
import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.ListDTO;
import com.ccoins.bff.dto.LongListDTO;
import com.ccoins.bff.dto.bars.BarTableDTO;
import com.ccoins.bff.dto.bars.TableDTO;
import com.ccoins.bff.dto.bars.TableQuantityDTO;
import com.ccoins.bff.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ITablesService {

    ResponseEntity<TableDTO> saveOrUpdate(TableDTO request);
    ResponseEntity<BarTableDTO> findById(IdDTO request);

    ResponseEntity<ListDTO> findAllByBar(IdDTO request, Optional<String> status);

    ResponseEntity<TableDTO> active(IdDTO request);

    ResponseEntity<GenericRsDTO> createByQuantity(TableQuantityDTO request);

    ResponseEntity<GenericRsDTO> deleteByQuantity(TableQuantityDTO request);

    ResponseEntity<GenericRsDTO> activeByList(LongListDTO request);

    ResponseEntity<ResponseDTO> generateCodesByList(LongListDTO request);

    List<BarTableDTO> findByIdIn(LongListDTO list);

    BarTableDTO findByCode(String code);

    boolean isTableActiveByCode(String code);
}
