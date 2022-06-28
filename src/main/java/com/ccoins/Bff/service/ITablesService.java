package com.ccoins.Bff.service;

import com.ccoins.Bff.dto.GenericRsDTO;
import com.ccoins.Bff.dto.IdDTO;
import com.ccoins.Bff.dto.ListDTO;
import com.ccoins.Bff.dto.bars.BarTableDTO;
import com.ccoins.Bff.dto.bars.TableDTO;
import com.ccoins.Bff.dto.bars.TableQuantityDTO;
import com.ccoins.Bff.exceptions.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ITablesService {

    ResponseEntity<TableDTO> saveOrUpdate(TableDTO request);
    ResponseEntity<TableDTO> findById(IdDTO request);

    ResponseEntity<ListDTO> findAllByBar(IdDTO request, Optional<String> status);

    ResponseEntity<TableDTO> active(IdDTO request);

    ResponseEntity<GenericRsDTO> createByQuantity(TableQuantityDTO request);

    ResponseEntity<GenericRsDTO> deleteByQuantity(TableQuantityDTO request);

    ResponseEntity<GenericRsDTO> activeByList(ListDTO request);

    ResponseEntity<ResponseDTO> generateCodesByList(ListDTO request);

    List<BarTableDTO> findByIdIn(List<Long> list);
}
