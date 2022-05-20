package com.ccoins.Bff.controller;

import com.ccoins.Bff.controller.swagger.IBarsController;
import com.ccoins.Bff.dto.IdDTO;
import com.ccoins.Bff.dto.ListDTO;
import com.ccoins.Bff.dto.bars.BarDTO;
import com.ccoins.Bff.service.IBarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bars")
@CrossOrigin
public class BarsController implements IBarsController {

    private final IBarsService barService;

    @Autowired
    public BarsController(IBarsService barService) {
        this.barService = barService;
    }

    //save or update
    @Override
    @PostMapping("/save")
    public ResponseEntity<BarDTO> saveOrUpdate(@RequestBody BarDTO barDTO, @RequestHeader HttpHeaders headers){
        return this.barService.saveOrUpdate(barDTO, headers);
    }

    //find by id
    @Override
    @GetMapping
    public ResponseEntity<BarDTO> findById(@RequestBody IdDTO id, @RequestHeader HttpHeaders headers){
        return this.barService.findById(id, headers);
    }

    //find all by owner
    @Override
    @GetMapping("/owner")
    public ResponseEntity<ListDTO> findAllByOwner(@RequestHeader HttpHeaders headers){
        return this.barService.findAllByOwner(headers);
    }
}
