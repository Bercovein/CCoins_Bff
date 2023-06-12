package com.ccoins.bff.service;

import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.ListDTO;
import com.ccoins.bff.dto.ResponseDTO;
import com.ccoins.bff.dto.prizes.PrizeDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface IPrizesService {

    ResponseEntity<PrizeDTO> saveOrUpdate(PrizeDTO request);

    ResponseEntity<PrizeDTO> findById(IdDTO id);

    ResponseEntity<ListDTO> findAllByBar(IdDTO request, Optional<String> status);

    ResponseEntity<PrizeDTO> active(IdDTO id);

    ResponseEntity<ResponseDTO> buyPrizeByTableAndUser(IdDTO idDTO, String client, String code);

    ResponseEntity<ListDTO> findAllByHeader(HttpHeaders headers);

}
