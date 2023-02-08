package com.ccoins.bff.service.impl;

import com.ccoins.bff.dto.ResponseDTO;
import com.ccoins.bff.dto.coins.SpendCoinsRqDTO;
import com.ccoins.bff.exceptions.BadRequestException;
import com.ccoins.bff.exceptions.constant.ExceptionConstant;
import com.ccoins.bff.feign.CoinsFeign;
import com.ccoins.bff.service.ICoinsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CoinsService implements ICoinsService {

    private final CoinsFeign feign;

    @Autowired
    public CoinsService(CoinsFeign feign) {
        this.feign = feign;
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


        }catch (Exception e){
            throw e;
        }
        return response;
    }
}
