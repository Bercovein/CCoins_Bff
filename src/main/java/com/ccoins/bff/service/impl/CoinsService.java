package com.ccoins.bff.service.impl;

import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.ResponseDTO;
import com.ccoins.bff.dto.coins.CoinsReportDTO;
import com.ccoins.bff.dto.coins.SpendCoinsRqDTO;
import com.ccoins.bff.dto.prizes.PartyDTO;
import com.ccoins.bff.exceptions.BadRequestException;
import com.ccoins.bff.exceptions.constant.ExceptionConstant;
import com.ccoins.bff.feign.CoinsFeign;
import com.ccoins.bff.feign.PrizeFeign;
import com.ccoins.bff.service.ICoinsService;
import com.ccoins.bff.service.IServerSentEventService;
import com.ccoins.bff.utils.HeaderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.ccoins.bff.utils.enums.EventNamesCoinsEnum.UPDATE_COINS;

@Service
public class CoinsService implements ICoinsService {

    private final CoinsFeign coinsFeign;

    private final PrizeFeign prizeFeign;

    private final IServerSentEventService sseService;

    @Autowired
    public CoinsService(CoinsFeign coinsFeign, PrizeFeign prizeFeign, IServerSentEventService sseService) {
        this.coinsFeign = coinsFeign;
        this.prizeFeign = prizeFeign;
        this.sseService = sseService;
    }

    @Override
    public Long countCoinsByParty(Long id) {

        try{
            return this.coinsFeign.countCoinsByParty(id).getBody();
        }catch (Exception e){
            throw new BadRequestException(ExceptionConstant.COUNT_COINS_BY_PARTY_ERROR_CODE,
                    this.getClass(), ExceptionConstant.COUNT_COINS_BY_PARTY_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> spendCoinsInPrizeByParty(Long partyId, Long clientParty, Long prizeId, Long prizePoints){
        ResponseEntity<ResponseDTO> response;

        try{
            response = this.coinsFeign.spendCoinsInPrizeByParty(
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

        Long partyId = HeaderUtils.getPartyId(headers);
        return this.getCoinsReportByParty(partyId, pagination,type);

    }

    @Override
    public ResponseEntity<CoinsReportDTO> getCoinsReport(IdDTO tableId, Pageable pagination, String type) {

        Optional<PartyDTO> partyOpt = this.prizeFeign.findActivePartyByTable(tableId.getId());

        if(partyOpt.isEmpty()){
            return ResponseEntity.badRequest().body(null);
        }

        PartyDTO party = partyOpt.get();

        return this.getCoinsReportByParty(party.getId(), pagination,type);
    }

    @Override
    public ResponseEntity<CoinsReportDTO> getCoinsReportByParty(Long partyId, Pageable pagination, String type){
        try{
            ResponseEntity<CoinsReportDTO> response = this.coinsFeign.getAllCoinsFromParty(partyId, pagination, type);
            return response;
        }catch (Exception e){
            throw new BadRequestException(ExceptionConstant.COINS_BY_PARTY_ERROR_CODE,
                    this.getClass(), ExceptionConstant.COINS_BY_PARTY_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<String>> getActiveStates() {

        try{
            return this.coinsFeign.getActiveStates();
        }catch (Exception e){
            throw new BadRequestException(ExceptionConstant.GET_COIN_STATES_ERROR_CODE,
                    this.getClass(), ExceptionConstant.GET_COIN_STATES_ERROR);
        }
    }
}
