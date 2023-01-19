package com.ccoins.bff.controller;

import com.ccoins.bff.service.ICoinsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coins")
public class SongVotesController {

    private ICoinsService service;

}
