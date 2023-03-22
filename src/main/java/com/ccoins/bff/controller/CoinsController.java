package com.ccoins.bff.controller;

import com.ccoins.bff.dto.coins.CoinsReportDTO;
import com.ccoins.bff.service.ICoinsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/coins")
public class CoinsController {

    @Autowired
    private ICoinsService service;

    @GetMapping("/party/report")
    public ResponseEntity<CoinsReportDTO> getCoinsReport(@RequestParam(value = "type", required = false) String type,
                                                        @RequestHeader HttpHeaders headers,
                                                        @PageableDefault(page = 0, size = 10, sort = "date") Pageable pagination){
        return this.service.getCoinsReport(headers, pagination, type);
    }
}
