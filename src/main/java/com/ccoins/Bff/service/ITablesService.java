package com.ccoins.Bff.service;

import com.ccoins.Bff.dto.GenericRsDTO;
import com.ccoins.Bff.dto.IdDTO;
import com.ccoins.Bff.dto.ListDTO;
import com.ccoins.Bff.dto.bars.TableDTO;
import com.ccoins.Bff.dto.bars.TableQuantityDTO;
import org.springframework.http.ResponseEntity;

public interface ITablesService {

    ResponseEntity<TableDTO> saveOrUpdate(TableDTO request);
    ResponseEntity<TableDTO> findById(IdDTO request);
    ResponseEntity<ListDTO> findAllByBar(IdDTO request);
    ResponseEntity<TableDTO> active(IdDTO request);

    ResponseEntity<GenericRsDTO> createByQuantity(TableQuantityDTO request);

    ResponseEntity<GenericRsDTO> deleteByQuantity(TableQuantityDTO request);

    ResponseEntity<GenericRsDTO> activeByList(ListDTO request);
}
