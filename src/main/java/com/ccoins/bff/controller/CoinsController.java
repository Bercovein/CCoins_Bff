package com.ccoins.bff.controller;

import com.ccoins.bff.dto.coins.CoinsReportDTO;
import com.ccoins.bff.service.ICoinsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coins")
public class CoinsController {

    @Autowired
    private ICoinsService service;

    @GetMapping("/party/report")
    public ResponseEntity<CoinsReportDTO> getCoinsReport(@RequestHeader HttpHeaders headers){
        return this.service.getCoinsReport(headers);
    }
}
