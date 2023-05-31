package com.ccoins.bff.service;

import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.StringDTO;
import com.ccoins.bff.dto.bars.BarDTO;
import com.ccoins.bff.dto.bars.BarListDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface IBarsService {

    ResponseEntity<BarDTO> saveOrUpdate(BarDTO barDTO);

    ResponseEntity<BarDTO> findById(IdDTO id);

    ResponseEntity<BarListDTO> findAllByOwner();

    ResponseEntity<BarDTO> active(IdDTO id);

    ResponseEntity<StringDTO> findUrlMenu(HttpHeaders headers);

}
