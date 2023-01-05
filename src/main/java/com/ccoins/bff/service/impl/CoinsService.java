package com.ccoins.bff.service.impl;

import com.ccoins.bff.exceptions.BadRequestException;
import com.ccoins.bff.exceptions.constant.ExceptionConstant;
import com.ccoins.bff.feign.CoinsFeign;
import com.ccoins.bff.service.ICoinsService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
