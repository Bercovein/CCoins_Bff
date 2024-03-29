package com.ccoins.bff.controller;

import com.ccoins.bff.controller.swagger.ICoinsController;
import com.ccoins.bff.dto.GenericRsDTO;
import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.LongDTO;
import com.ccoins.bff.dto.coins.CoinsReportDTO;
import com.ccoins.bff.dto.coins.CoinsReportStatesDTO;
import com.ccoins.bff.dto.coins.StateDTO;
import com.ccoins.bff.service.ICoinsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coins")
public class CoinsController implements ICoinsController {

    private final ICoinsService service;

    @Autowired
    public CoinsController(ICoinsService service) {
        this.service = service;
    }

    @GetMapping("/party/report")
    @Override
    public ResponseEntity<CoinsReportDTO> getCoinsReportByTableId(@RequestParam(value = "type", required = false) String type,
                                                                  @RequestHeader HttpHeaders headers,
                                                                  @PageableDefault(page = 0, size = 10, sort = "date") Pageable pagination){
        return this.service.getCoinsReport(headers, pagination, type);
    }

    @PostMapping("/party/report")
    @Override
    public ResponseEntity<CoinsReportDTO> getCoinsReportByTableId(@RequestParam(value = "type", required = false) String type,
                                                                  @RequestBody IdDTO tableId,
                                                                  @PageableDefault(page = 0, size = 10, sort = "date") Pageable pagination){
        return this.service.getCoinsReport(tableId, pagination, type);
    }

    @GetMapping("/active-states")
    @Override
    public ResponseEntity<List<StateDTO>> getActiveStates(){
        return this.service.getActiveStates();
    }

    @PostMapping("/deliver")
    @Override
    public ResponseEntity<GenericRsDTO<Long>> deliverPrizeOrCoins(@RequestBody IdDTO id){
        return this.service.deliverPrizeOrCoins(id.getId());
    }

    @PostMapping("/cancel")
    @Override
    public ResponseEntity<GenericRsDTO<Long>> cancelPrizeOrCoins(@RequestBody IdDTO id){
        return this.service.cancelPrizeOrCoins(id.getId());
    }

    @PostMapping("/adjust")
    @Override
    public ResponseEntity<GenericRsDTO<Long>> adjustPrizeOrCoins(@RequestBody IdDTO id){
        return this.service.adjustPrizeOrCoins(id.getId());
    }

    @GetMapping("/report/in-demand")
    @Override
    public ResponseEntity<GenericRsDTO<List<CoinsReportStatesDTO>>> getInDemandReport(){
        return this.service.getInDemandReport();
    }

    @GetMapping("/report/out-demand")
    @Override
    public ResponseEntity<GenericRsDTO<List<CoinsReportStatesDTO>>> getNotDemandedReport(){
        return this.service.getNotDemandedReport();
    }

    @GetMapping("/count-demand")
    @Override
    public ResponseEntity<LongDTO> countInDemandReport(){
        return this.service.countInDemandReport();
    }
}
