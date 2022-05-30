package com.ccoins.Bff.service;

import com.ccoins.Bff.dto.IdDTO;
import com.ccoins.Bff.dto.ListDTO;
import com.ccoins.Bff.dto.bars.TableDTO;
import org.springframework.http.ResponseEntity;

public interface ITableService {

    ResponseEntity<TableDTO> saveOrUpdate(TableDTO request);
    ResponseEntity<TableDTO> findById(IdDTO request);
    ResponseEntity<ListDTO> findAllByBar(IdDTO request);
    ResponseEntity<TableDTO> active(IdDTO request);
}
