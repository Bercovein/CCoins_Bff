package com.ccoins.bff.service.impl;

import com.ccoins.bff.dto.GenericRsDTO;
import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.LongDTO;
import com.ccoins.bff.dto.ResponseDTO;
import com.ccoins.bff.dto.coins.CoinsReportDTO;
import com.ccoins.bff.dto.coins.CoinsReportStatesDTO;
import com.ccoins.bff.dto.coins.SpendCoinsRqDTO;
import com.ccoins.bff.dto.coins.StateDTO;
import com.ccoins.bff.dto.prizes.PartyDTO;
import com.ccoins.bff.exceptions.BadRequestException;
import com.ccoins.bff.exceptions.constant.ExceptionConstant;
import com.ccoins.bff.feign.BarsFeign;
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

import static com.ccoins.bff.utils.enums.EventNamesEnum.NEW_DEMAND;
import static com.ccoins.bff.utils.enums.EventNamesEnum.UPDATE_COINS;
import static com.ccoins.bff.utils.enums.UpdateCoinsMessagesEnum.*;

@Service
public class CoinsService extends ContextService implements ICoinsService {

    private final CoinsFeign coinsFeign;

    private final PrizeFeign prizeFeign;

    private final IServerSentEventService sseService;

    @Autowired
    public CoinsService(CoinsFeign coinsFeign, PrizeFeign prizeFeign, IServerSentEventService sseService, BarsFeign barsFeign) {
        super(barsFeign);
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
        }catch (Exception e){
            throw e;
        }
            try {
                if (HttpStatus.OK.equals(response.getStatusCode())) {
                    this.sseService.dispatchEventToClientsFromParty(UPDATE_COINS.name(), PRIZE_SPEND_COINS.getMessage(), partyId);

                    ResponseEntity<IdDTO> barResponse = this.barsFeign.getBarIdByParty(partyId);

                    if (barResponse.hasBody() && barResponse.getBody() != null) {
                        Long barId = barResponse.getBody().getId();
                        this.sseService.dispatchEventToSingleBar(NEW_DEMAND.name(), NEW_DEMAND.getMessage(), barId);
                    }
                }
            }catch (Exception ignored){}

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
            return ResponseEntity.ok(new CoinsReportDTO(0L,null));
        }

        PartyDTO party = partyOpt.get();

        return this.getCoinsReportByParty(party.getId(), pagination,type);
    }

    @Override
    public ResponseEntity<CoinsReportDTO> getCoinsReportByParty(Long partyId, Pageable pagination, String type){
        try{
            return this.coinsFeign.getAllCoinsFromParty(partyId, pagination, type);
        }catch (Exception e){
            throw new BadRequestException(ExceptionConstant.COINS_BY_PARTY_ERROR_CODE,
                    this.getClass(), ExceptionConstant.COINS_BY_PARTY_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<StateDTO>> getActiveStates() {

        try{
            return this.coinsFeign.getActiveStates();
        }catch (Exception e){
            throw new BadRequestException(ExceptionConstant.GET_COIN_STATES_ERROR_CODE,
                    this.getClass(), ExceptionConstant.GET_COIN_STATES_ERROR);
        }
    }

    @Override
    public ResponseEntity<GenericRsDTO<Long>> deliverPrizeOrCoins(Long id){
        try{
            ResponseEntity<GenericRsDTO<Long>> response =  this.coinsFeign.deliverPrizeOrCoins(id);

            if(response.hasBody()){
                GenericRsDTO<Long> generic = response.getBody();

                assert generic != null;
                Long partyId = generic.getData();

                if(partyId != null){
                    this.sseService.dispatchEventToClientsFromParty(UPDATE_COINS.name(),DELIVER_PRIZE_OR_COINS.getMessage(),partyId);
                }
            }
            return response;
        }catch (Exception e){
            throw new BadRequestException(ExceptionConstant.COIN_STATES_ERROR_CODE,
                    this.getClass(), ExceptionConstant.COIN_STATES_ERROR);
        }
    }

    @Override
    public ResponseEntity<GenericRsDTO<Long>> cancelPrizeOrCoins(Long id){
        try{
            ResponseEntity<GenericRsDTO<Long>> response = this.coinsFeign.cancelPrizeOrCoins(id);

            if(response.hasBody()){
                GenericRsDTO<Long> generic = response.getBody();

                Long partyId = generic.getData();

                if(partyId != null){
                    this.sseService.dispatchEventToClientsFromParty(UPDATE_COINS.name(),CANCEL_PRIZE_OR_COINS.getMessage(),partyId);
                }
            }
            return response;
        }catch (Exception e){
            throw new BadRequestException(ExceptionConstant.COIN_STATES_ERROR_CODE,
                    this.getClass(), ExceptionConstant.COIN_STATES_ERROR);
        }
    }

    @Override
    public ResponseEntity<GenericRsDTO<Long>> adjustPrizeOrCoins(Long id){
        try{
            ResponseEntity<GenericRsDTO<Long>> response = this.coinsFeign.adjustPrizeOrCoins(id);

            if(response.hasBody()){
                GenericRsDTO<Long> generic = response.getBody();

                Long partyId = generic.getData();

                if(partyId != null){
                    this.sseService.dispatchEventToClientsFromParty(UPDATE_COINS.name(),ADJUST_PRIZE_OR_COINS.getMessage(),partyId);
                }
            }
            return response;
        }catch (Exception e){
            throw new BadRequestException(ExceptionConstant.COIN_STATES_ERROR_CODE,
                    this.getClass(), ExceptionConstant.COIN_STATES_ERROR);
        }
    }

    @Override
    public ResponseEntity<GenericRsDTO<List<CoinsReportStatesDTO>>> getInDemandReport(){

        try{
            return this.coinsFeign.getInDemandReport(super.findBarIdByOwner());
        }catch (Exception e){
            throw new BadRequestException(ExceptionConstant.COIN_STATE_REPORT_ERROR_CODE,
                    this.getClass(), ExceptionConstant.COIN_STATE_REPORT_ERROR);
        }
    }

    @Override
    public ResponseEntity<GenericRsDTO<List<CoinsReportStatesDTO>>> getNotDemandedReport(){

        try{
            return this.coinsFeign.getNotDemandedReport(super.findBarIdByOwner());

        }catch (Exception e){
            throw new BadRequestException(ExceptionConstant.COIN_STATE_REPORT_ERROR_CODE,
                    this.getClass(), ExceptionConstant.COIN_STATE_REPORT_ERROR);
        }
    }

    @Override
    public ResponseEntity<LongDTO> countInDemandReport() {

        try{
            Long barId = super.findBarIdByOwner();
            return this.coinsFeign.countInDemandReport(barId);
        }catch (Exception e){
            return ResponseEntity.ok(LongDTO.builder().value(0L).build());
        }
    }
}
