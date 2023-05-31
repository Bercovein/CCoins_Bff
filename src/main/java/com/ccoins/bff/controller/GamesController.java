package com.ccoins.bff.controller;

import com.ccoins.bff.controller.swagger.IGamesController;
import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.ListDTO;
import com.ccoins.bff.dto.bars.GameDTO;
import com.ccoins.bff.service.IGamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/games")
@CrossOrigin
public class GamesController implements IGamesController {

    private final IGamesService service;

    @Autowired
    public GamesController(IGamesService service) {
        this.service = service;
    }

    @Override
    @PostMapping("/save")
    @ResponseStatus(CREATED)
    public ResponseEntity<GameDTO> saveOrUpdate(@RequestBody GameDTO request){
        return this.service.saveOrUpdate(request);
    }

    @Override
    @PostMapping("/id")
    public ResponseEntity<GameDTO> findById(@RequestBody IdDTO request){
        return this.service.findById(request);
    }

    @Override
    @GetMapping("/bar/{id}")
    public ResponseEntity<ListDTO> findAllByBar(@PathVariable("id") Long id){
        return this.service.findAllByBar(id);
    }

    @Override
    @PatchMapping("/active")
    public ResponseEntity<GameDTO> active(@RequestBody IdDTO request){
        return this.service.active(request);
    }

    @GetMapping("/types")
    @Override
    public ResponseEntity<ListDTO> findAllTypes() {
        return this.service.findAllGamesTypes();
    }
}
