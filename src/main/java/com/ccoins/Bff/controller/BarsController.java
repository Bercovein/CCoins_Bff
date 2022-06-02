package com.ccoins.Bff.controller;

import com.ccoins.Bff.controller.swagger.IBarsController;
import com.ccoins.Bff.dto.IdDTO;
import com.ccoins.Bff.dto.ListDTO;
import com.ccoins.Bff.dto.bars.BarDTO;
import com.ccoins.Bff.service.IBarsService;
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
    public ResponseEntity<ListDTO> findAllByOwner(){
        return this.service.findAllByOwner();
    }

    @Override
    @PatchMapping("/active")
    public ResponseEntity<BarDTO> active(@RequestBody IdDTO request){
        return this.service.active(request);
    }
}
