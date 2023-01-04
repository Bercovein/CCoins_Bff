package com.ccoins.bff.controller;

import com.ccoins.bff.controller.swagger.ISpotifyController;
import com.ccoins.bff.modal.PlaylistSPTF;
import com.ccoins.bff.service.ISpotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/spotify")
public class SpotifyController implements ISpotifyController {

    @Autowired
    private ISpotifyService service;

    @GetMapping("/playlist")
    @Override
    public ResponseEntity<PlaylistSPTF> getPlaylist(@RequestHeader HttpHeaders headers){
        return this.service.getPlaylist(headers);
    }
}
