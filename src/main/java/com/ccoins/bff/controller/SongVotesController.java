package com.ccoins.bff.controller;

import com.ccoins.bff.controller.swagger.ISongVotesController;
import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.service.IVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/votes")
public class SongVotesController implements ISongVotesController {

    private final IVoteService service;

    @Autowired
    public SongVotesController(IVoteService service) {
        this.service = service;
    }

    @Override
    @PostMapping
    public void voteSong(@RequestHeader HttpHeaders headers, @RequestBody IdDTO request){
        this.service.voteSong(headers, request);
    }
}
