package com.ccoins.bff.controller;

import com.ccoins.bff.annotation.LimitedTime;
import com.ccoins.bff.controller.swagger.IPartyController;
import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.ListDTO;
import com.ccoins.bff.dto.LongDTO;
import com.ccoins.bff.dto.ResponseDTO;
import com.ccoins.bff.dto.prizes.PartyDTO;
import com.ccoins.bff.service.IPartiesService;
import com.ccoins.bff.service.IPrizesService;
import com.ccoins.bff.utils.HeaderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parties")
@CrossOrigin
public class PartyController implements IPartyController {

    private final IPartiesService service;
    private final IPrizesService prizeService;

    @Autowired
    public PartyController(IPartiesService service, IPrizesService prizeService) {
        this.service = service;
        this.prizeService = prizeService;
    }

    @PostMapping("/coins/quantity")
    @LimitedTime
    @Override
    public ResponseEntity<LongDTO> getTotalCoinsByParty(@RequestBody IdDTO id){
        return ResponseEntity.ok(this.service.countCoinsByParty(id.getId()));
    }

    @PostMapping("")
    @LimitedTime
    @Override
    public ResponseEntity<PartyDTO> getPartyInfo(@RequestBody IdDTO id){
        return ResponseEntity.ok(this.service.findById(id.getId()));
    }

    //integrantes de la mesa
    @PostMapping("/clients")
    @LimitedTime
    @Override
    public ResponseEntity<ListDTO> getClientsFromParty(@RequestBody IdDTO id, @RequestHeader HttpHeaders headers){
        return ResponseEntity.ok(this.service.findClientsFromParty(id.getId(), headers));
    }

    @Override
    @PostMapping("/prizes/buy")
    @LimitedTime
    public ResponseEntity<ResponseDTO> buyPrize(@RequestBody IdDTO idDTO, @RequestHeader HttpHeaders headers){
        return this.prizeService.buyPrizeByTableAndUser(idDTO, HeaderUtils.getClient(headers), HeaderUtils.getCode(headers));
    }

    @Override
    @GetMapping({"/bar-prizes"})
    public ResponseEntity<ListDTO> findAllByBar(@RequestHeader HttpHeaders headers){

        return this.prizeService.findAllByHeader(headers);
    }

}
