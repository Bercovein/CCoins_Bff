package com.ccoins.bff.controller;

import com.ccoins.bff.controller.swagger.IPartyTablesController;
import com.ccoins.bff.dto.PartyTableDTO;
import com.ccoins.bff.service.IPartiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/party-tables")
@CrossOrigin
public class PartyTablesController implements IPartyTablesController {

    private final IPartiesService partiesService;

    @Autowired
    public PartyTablesController(IPartiesService partiesService) {
        this.partiesService = partiesService;
    }

    @Override
    @GetMapping
    public ResponseEntity<List<PartyTableDTO>> findActivePartiesByOwner(){
        return this.partiesService.findActivePartiesByOwner();
    }
}
