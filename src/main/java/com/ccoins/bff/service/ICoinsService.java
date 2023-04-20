package com.ccoins.bff.service;

import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.ResponseDTO;
import com.ccoins.bff.dto.coins.CoinsReportDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ICoinsService {

    Long countCoinsByParty(Long id);

    ResponseEntity<ResponseDTO> spendCoinsInPrizeByParty(Long partyId, Long clientParty, Long prizeId, Long prizePoints);

    ResponseEntity<CoinsReportDTO> getCoinsReport(HttpHeaders headers, Pageable pagination, String type);

    ResponseEntity<CoinsReportDTO> getCoinsReport(IdDTO tableId, Pageable pagination, String type);

    ResponseEntity<CoinsReportDTO> getCoinsReportByParty(Long partyId, Pageable pagination, String type);

    ResponseEntity<List<String>> getActiveStates();
}