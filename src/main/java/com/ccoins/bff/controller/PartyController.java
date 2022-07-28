package com.ccoins.bff.controller;

import com.ccoins.bff.controller.swagger.IPartyController;
import com.ccoins.bff.dto.LongDTO;
import com.ccoins.bff.service.IPartiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<LongDTO> getTotalCoinsByParty(Long id){
        return ResponseEntity.ok(this.service.countCoinsByParty(id));
    }

    //info de la mesa (nombre de la party, nro de mesa..)

    //integrantes de la mesa


}
