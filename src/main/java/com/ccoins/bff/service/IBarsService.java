package com.ccoins.bff.service;

import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.ListDTO;
import com.ccoins.bff.dto.bars.BarDTO;
import org.springframework.http.ResponseEntity;

public interface IBarsService {

    ResponseEntity<BarDTO> saveOrUpdate(BarDTO barDTO);

    ResponseEntity<BarDTO> findById(IdDTO id);

    ResponseEntity<ListDTO> findAllByOwner();

    ResponseEntity<BarDTO> active(IdDTO id);
}