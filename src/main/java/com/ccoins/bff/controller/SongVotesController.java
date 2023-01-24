package com.ccoins.bff.controller;

import com.ccoins.bff.controller.swagger.ISongVotesController;
import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.service.ICoinsService;
import com.ccoins.bff.service.IVoteService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/votes")
public class SongVotesController implements ISongVotesController {

    private IVoteService service;

    @Override
    public void voteSong(@RequestHeader HttpHeaders headers, @RequestBody IdDTO request){
        this.service.voteSong(headers, request);
    }
}
