package com.ccoins.Bff.service.impl;

import com.ccoins.Bff.feign.CoinsFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CoinsServiceImpl implements ICoinsService{

    private final CoinsFeign coinsRepository;

    @Autowired
    public CoinsServiceImpl(CoinsFeign coinsRepository) {
        this.coinsRepository = coinsRepository;
    }

    @Override
    public ResponseEntity demo(){
        return this.coinsRepository.demo();
    }
}
