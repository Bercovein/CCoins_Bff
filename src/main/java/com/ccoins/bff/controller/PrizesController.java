package com.ccoins.bff.controller;

import com.ccoins.bff.controller.swagger.IPrizesController;
import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.ListDTO;
import com.ccoins.bff.dto.prizes.PrizeDTO;
import com.ccoins.bff.service.IPrizesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/prizes")
@CrossOrigin
public class PrizesController implements IPrizesController {

    private final IPrizesService service;

    @Autowired
    public PrizesController(IPrizesService service) {
        this.service = service;
    }

    @Override
    @PostMapping("/save")
    @ResponseStatus(CREATED)
    public ResponseEntity<PrizeDTO> saveOrUpdate(@RequestBody PrizeDTO request){
        return this.service.saveOrUpdate(request);
    }

    @Override
    @PostMapping("/id")
    public ResponseEntity<PrizeDTO> findById(@RequestBody IdDTO request){
        return this.service.findById(request);
    }

    @Override
    @PostMapping({"/bar", "/bar/{status}"})
    public ResponseEntity<ListDTO> findAllByBar(@RequestBody IdDTO request, @PathVariable("status") Optional<String> status){
        return this.service.findAllByBar(request,status);
    }

    @Override
    @PatchMapping("/active")
    public ResponseEntity<PrizeDTO> active(@RequestBody IdDTO request){
        return this.service.active(request);
    }
}
