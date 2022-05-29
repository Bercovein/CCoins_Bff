package com.ccoins.Bff.controller;

import com.ccoins.Bff.controller.swagger.ITablesController;
import com.ccoins.Bff.dto.IdDTO;
import com.ccoins.Bff.dto.ListDTO;
import com.ccoins.Bff.dto.bars.TableDTO;
import com.ccoins.Bff.service.ITablesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tables")
@CrossOrigin
public class TablesController implements ITablesController {

    private final ITablesService service;

    @Autowired
    public TablesController(ITablesService service) {
        this.service = service;
    }

    //save or update
    @Override
    @PostMapping("/save")
    public ResponseEntity<TableDTO> saveOrUpdate(@RequestBody TableDTO request){
        return this.service.saveOrUpdate(request);
    }

    //find by id
    @Override
    @GetMapping
    public ResponseEntity<TableDTO> findById(@RequestBody IdDTO id){
        return this.service.findById(id);
    }

    //find all by owner
    @Override
    @GetMapping("/bar")
    public ResponseEntity<ListDTO> findAllByBar(@RequestBody IdDTO request){
        return this.service.findAllByBar(request);
    }

    //delete logico
    @Override
    @GetMapping("/active")
    public ResponseEntity<TableDTO> active(@RequestBody IdDTO id){
        return this.service.active(id);
    }
}
