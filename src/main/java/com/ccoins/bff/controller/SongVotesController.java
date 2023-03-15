package com.ccoins.bff.controller;

import com.ccoins.bff.annotation.LimitedTime;
import com.ccoins.bff.controller.swagger.ISongVotesController;
import com.ccoins.bff.dto.GenericRsDTO;
import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.ResponseDTO;
import com.ccoins.bff.service.IVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
    @LimitedTime
    public void voteSong(@RequestHeader HttpHeaders headers, @RequestBody IdDTO request){
        this.service.voteSong(headers, request);
    }

    @Override
    @GetMapping("/check-vote")
    @LimitedTime
    public ResponseEntity<GenericRsDTO<ResponseDTO>> checkVote(@RequestHeader HttpHeaders headers){
        return this.service.checkVote(headers);
    }

}
