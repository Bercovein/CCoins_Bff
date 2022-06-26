package com.ccoins.Bff.service;

import com.ccoins.Bff.dto.IdDTO;
import com.ccoins.Bff.dto.ListDTO;
import com.ccoins.Bff.dto.prizes.PrizeDTO;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface IPrizesService {

    ResponseEntity<PrizeDTO> saveOrUpdate(PrizeDTO request);

    ResponseEntity<PrizeDTO> findById(IdDTO id);

    ResponseEntity<ListDTO> findAllByBar(IdDTO request, Optional<String> status);

    ResponseEntity<PrizeDTO> active(IdDTO id);
}
