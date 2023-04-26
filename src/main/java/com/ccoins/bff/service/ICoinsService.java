package com.ccoins.bff.service;

import com.ccoins.bff.dto.GenericRsDTO;
import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.LongDTO;
import com.ccoins.bff.dto.ResponseDTO;
import com.ccoins.bff.dto.coins.CoinsReportDTO;
import com.ccoins.bff.dto.coins.CoinsReportStatesDTO;
import com.ccoins.bff.dto.coins.StateDTO;
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

    ResponseEntity<List<StateDTO>> getActiveStates();

    ResponseEntity<GenericRsDTO<Long>> deliverPrizeOrCoins(Long id);

    ResponseEntity<GenericRsDTO<Long>> cancelPrizeOrCoins(Long id);

    ResponseEntity<GenericRsDTO<Long>> adjustPrizeOrCoins(Long id);

    ResponseEntity<GenericRsDTO<List<CoinsReportStatesDTO>>> getInDemandReport();

    ResponseEntity<GenericRsDTO<List<CoinsReportStatesDTO>>> getNotDemandedReport();

    ResponseEntity<LongDTO> countInDemandReport();
}