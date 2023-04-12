package com.ccoins.bff.controller;

import com.ccoins.bff.controller.swagger.IBarsController;
import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.bars.BarDTO;
import com.ccoins.bff.dto.bars.BarListDTO;
import com.ccoins.bff.service.IBarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/bars")
@CrossOrigin
public class BarsController implements IBarsController {

    private final IBarsService service;

    @Autowired
    public BarsController(IBarsService service) {
        this.service = service;
    }

    @Override
    @PostMapping("/save")
    @ResponseStatus(CREATED)
    public ResponseEntity<BarDTO> saveOrUpdate(@RequestBody BarDTO request){
        return this.service.saveOrUpdate(request);
    }

    @Override
    @PostMapping("/id")
    public ResponseEntity<BarDTO> findById(@RequestBody IdDTO request){
        return this.service.findById(request);
    }

    @Override
    @GetMapping("/owner")
    public ResponseEntity<BarListDTO> findAllByOwner(){
        return this.service.findAllByOwner();
    }

    @Override
    @PatchMapping("/active")
    public ResponseEntity<BarDTO> active(@RequestBody IdDTO request){
        return this.service.active(request);
    }


}
