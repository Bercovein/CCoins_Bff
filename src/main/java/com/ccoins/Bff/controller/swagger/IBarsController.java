package com.ccoins.Bff.controller.swagger;

import com.ccoins.Bff.dto.IdDTO;
import com.ccoins.Bff.dto.ListDTO;
import com.ccoins.Bff.dto.bars.BarDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface IBarsController {
    //save or update
    @PostMapping
    ResponseEntity<BarDTO> saveOrUpdate(@RequestBody BarDTO barDTO);

    //find by id
    @GetMapping
    ResponseEntity<BarDTO> findById(@RequestBody IdDTO id, @RequestHeader HttpHeaders headers);

    //find all by owner
    @GetMapping("/owner")
    ResponseEntity<ListDTO> findAllByOwner(@RequestHeader HttpHeaders headers);
}
