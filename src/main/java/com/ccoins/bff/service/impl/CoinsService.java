package com.ccoins.bff.service.impl;

import com.ccoins.bff.dto.ResponseDTO;
import com.ccoins.bff.dto.coins.CoinsReportDTO;
import com.ccoins.bff.dto.coins.SpendCoinsRqDTO;
import com.ccoins.bff.exceptions.BadRequestException;
import com.ccoins.bff.exceptions.constant.ExceptionConstant;
import com.ccoins.bff.feign.CoinsFeign;
import com.ccoins.bff.service.ICoinsService;
import com.ccoins.bff.service.IServerSentEventService;
import com.ccoins.bff.utils.HeaderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.ccoins.bff.utils.enums.EventNamesCoinsEnum.UPDATE_COINS;

@Service
public class CoinsService implements ICoinsService {

    private final CoinsFeign feign;
    private final IServerSentEventService sseService;

    @Autowired
    public CoinsService(CoinsFeign feign, IServerSentEventService sseService) {
        this.feign = feign;
        this.sseService = sseService;
    }

    @Override
    public Long countCoinsByParty(Long id) {

        try{
            return this.feign.countCoinsByParty(id).getBody();
        }catch (Exception e){
            throw new BadRequestException(ExceptionConstant.COUNT_COINS_BY_PARTY_ERROR_CODE,
                    this.getClass(), ExceptionConstant.COUNT_COINS_BY_PARTY_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> spendCoinsInPrizeByParty(Long partyId, Long clientParty, Long prizeId, Long prizePoints){
        ResponseEntity<ResponseDTO> response;

        try{
            response = this.feign.spendCoinsInPrizeByParty(
                    SpendCoinsRqDTO.builder()
                            .partyId(partyId)
                            .prizeId(prizeId)
                            .clientParty(clientParty)
                            .prizePoints(prizePoints)
                            .build()
            );

            if (HttpStatus.OK.equals(response.getStatusCode())){
                this.sseService.dispatchEventToClientsFromParty(UPDATE_COINS.name(),null,partyId);
            }

        }catch (Exception e){
            throw e;
        }
        return response;
    }

    @Override
    public ResponseEntity<CoinsReportDTO> getCoinsReport(HttpHeaders headers, Pageable pagination, String type) {

        try{
            return this.feign.getAllCoinsFromParty(HeaderUtils.getPartyId(headers), pagination, type);
        }catch (Exception e){
            throw new BadRequestException(ExceptionConstant.COINS_BY_PARTY_ERROR_CODE,
                    this.getClass(), ExceptionConstant.COINS_BY_PARTY_ERROR);
        }

    }
}
