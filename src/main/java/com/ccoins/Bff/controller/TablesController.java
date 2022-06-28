package com.ccoins.Bff.controller;

import com.ccoins.Bff.controller.swagger.ITablesController;
import com.ccoins.Bff.dto.GenericRsDTO;
import com.ccoins.Bff.dto.IdDTO;
import com.ccoins.Bff.dto.ListDTO;
import com.ccoins.Bff.dto.LongListDTO;
import com.ccoins.Bff.dto.bars.TableDTO;
import com.ccoins.Bff.dto.bars.TableQuantityDTO;
import com.ccoins.Bff.exceptions.dto.ResponseDTO;
import com.ccoins.Bff.service.ITablesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/tables")
@CrossOrigin
public class TablesController implements ITablesController {

    private final ITablesService service;

    @Autowired
    public TablesController(ITablesService service) {
        this.service = service;
    }

    @Override
    @PostMapping("/save")
    public ResponseEntity<TableDTO> saveOrUpdate(@RequestBody TableDTO request){
        return this.service.saveOrUpdate(request);
    }

    @Override
    @PostMapping("/id")
    public ResponseEntity<TableDTO> findById(@RequestBody IdDTO id){
        return this.service.findById(id);
    }

    @Override
    @PostMapping({"/bar", "/bar/{status}"})
    public ResponseEntity<ListDTO> findAllByBar(@RequestBody IdDTO request, @PathVariable("status") Optional<String> status){
        return this.service.findAllByBar(request,status);
    }

    @Override
    @PatchMapping("/active")
    public ResponseEntity<TableDTO> active(@RequestBody IdDTO id){
        return this.service.active(id);
    }

    @Override
    @PostMapping("/save/quantity")
    public ResponseEntity<GenericRsDTO> createByQuantity(@RequestBody TableQuantityDTO request){
        return this.service.createByQuantity(request);
    }

    @Override
    @DeleteMapping("/quantity")
    public ResponseEntity<GenericRsDTO> deleteByQuantity(@RequestBody TableQuantityDTO request){
        return this.service.deleteByQuantity(request);
    }

    @PutMapping
    ResponseEntity<GenericRsDTO> activeByList(@RequestBody LongListDTO request){
        return this.service.activeByList(request);
    }

    @PutMapping("/codes")
    ResponseEntity<ResponseDTO> generateCodesByList(@RequestBody LongListDTO request){
        return this.service.generateCodesByList(request);
    }
}
