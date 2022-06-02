package com.ccoins.Bff.service;

import com.ccoins.Bff.dto.IdDTO;
import com.ccoins.Bff.dto.ListDTO;
import com.ccoins.Bff.dto.prizes.PrizeDTO;
import org.springframework.http.ResponseEntity;

public interface IPrizesService {

    ResponseEntity<PrizeDTO> saveOrUpdate(PrizeDTO request);

    ResponseEntity<PrizeDTO> findById(IdDTO id);

    ResponseEntity<ListDTO> findAllByBar(IdDTO request);

    ResponseEntity<PrizeDTO> active(IdDTO id);
}
