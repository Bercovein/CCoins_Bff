package com.ccoins.bff.controller;

import com.ccoins.bff.controller.swagger.IPartyController;
import com.ccoins.bff.dto.ListDTO;
import com.ccoins.bff.dto.LongDTO;
import com.ccoins.bff.dto.prizes.PartyDTO;
import com.ccoins.bff.service.IPartiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parties")
@CrossOrigin
public class PartyController implements IPartyController {

    private final IPartiesService service;

    @Autowired
    public PartyController(IPartiesService service) {
        this.service = service;
    }

    @GetMapping("/{id}/coins/quantity")
    @Override
    public ResponseEntity<LongDTO> getTotalCoinsByParty(@PathVariable("id") Long id){
        return ResponseEntity.ok(this.service.countCoinsByParty(id));
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<PartyDTO> getPartyInfo(@PathVariable("id") Long id){
        return ResponseEntity.ok(this.service.findById(id));
    }

    //integrantes de la mesa
    @GetMapping("/{id}/clients")
    @Override
    public ResponseEntity<ListDTO> getClientsFromParty(@PathVariable("id") Long id, @RequestHeader HttpHeaders headers){
        return ResponseEntity.ok(this.service.findClientsFromParty(id, headers));
    }
}
