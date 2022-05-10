package com.ccoins.Bff.controller;

import com.ccoins.Bff.controller.swagger.ICoinsController;
import com.ccoins.Bff.service.impl.ICoinsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo2")
public class DemoController implements ICoinsController {

    private final ICoinsService coinsService;

    @Autowired
    public DemoController(ICoinsService coinsService) {
        this.coinsService = coinsService;
    }

    @GetMapping
    public ResponseEntity demo(){
        return this.coinsService.demo();
    }
}
