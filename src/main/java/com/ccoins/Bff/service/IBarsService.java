package com.ccoins.Bff.service;

import com.ccoins.Bff.dto.IdDTO;
import com.ccoins.Bff.dto.ListDTO;
import com.ccoins.Bff.dto.bars.BarDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface IBarsService {

    ResponseEntity<BarDTO> saveOrUpdate(BarDTO barDTO);

    ResponseEntity<BarDTO> findById(IdDTO id, HttpHeaders headers);

    ResponseEntity<ListDTO> findAllByOwner(HttpHeaders headers);
}
