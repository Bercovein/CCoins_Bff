package com.ccoins.bff.controller;

import com.ccoins.bff.annotation.LimitedTime;
import com.ccoins.bff.controller.swagger.IPartyController;
import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.ListDTO;
import com.ccoins.bff.dto.LongDTO;
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

    @GetMapping("/{id}/coins/quantity")
    @LimitedTime
    @Override
    public ResponseEntity<LongDTO> getTotalCoinsByParty(@PathVariable("id") Long id){
        return ResponseEntity.ok(this.service.countCoinsByParty(id));
    }

    @GetMapping("/{id}")
    @LimitedTime
    @Override
    public ResponseEntity<PartyDTO> getPartyInfo(@PathVariable("id") Long id){
        return ResponseEntity.ok(this.service.findById(id));
    }

    //integrantes de la mesa
    @GetMapping("/{id}/clients")
    @LimitedTime
    @Override
    public ResponseEntity<ListDTO> getClientsFromParty(@PathVariable("id") Long id, @RequestHeader HttpHeaders headers){
        return ResponseEntity.ok(this.service.findClientsFromParty(id, headers));
    }

    @Override
    @PostMapping("/prizes/buy")
    @LimitedTime
    public void buyPrize(@RequestBody IdDTO idDTO, @RequestHeader HttpHeaders headers){
        this.prizeService.buyPrizeByTableAndUser(idDTO, HeaderUtils.getClient(headers), HeaderUtils.getCode(headers));
    }

    @Override
    @GetMapping({"/bar-prizes"})
    public ResponseEntity<ListDTO> findAllByBar(@RequestHeader HttpHeaders headers){

        return this.prizeService.findAllByHeader(headers);
    }

}
