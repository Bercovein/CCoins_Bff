package com.ccoins.bff.service;

import com.ccoins.bff.dto.ResponseDTO;
import com.ccoins.bff.dto.coins.CoinsReportDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface ICoinsService {

    Long countCoinsByParty(Long id);

    ResponseEntity<ResponseDTO> spendCoinsInPrizeByParty(Long partyId, Long clientParty, Long prizeId, Long prizePoints);

    ResponseEntity<CoinsReportDTO> getCoinsReport(HttpHeaders headers);
}