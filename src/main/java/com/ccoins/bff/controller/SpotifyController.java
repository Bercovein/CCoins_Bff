package com.ccoins.bff.controller;

import com.ccoins.bff.controller.swagger.ISpotifyController;
import com.ccoins.bff.dto.StringDTO;
import com.ccoins.bff.service.ISpotifyService;
import com.ccoins.bff.spotify.sto.BarTokenDTO;
import com.ccoins.bff.spotify.sto.CredentialsSPTFDTO;
import com.ccoins.bff.spotify.sto.SongSPTF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/spotify")
public class SpotifyController implements ISpotifyController {

    @Autowired
    private ISpotifyService service;

    @GetMapping("/config")
    @Override
    public ResponseEntity<CredentialsSPTFDTO> getCredentials(){
        return ResponseEntity.ok(this.service.getCredentials());
    }

    @PostMapping("/actualSongs/bar")
    @Override
    public void sendPlaybackToClients(@RequestBody @Valid BarTokenDTO request){
        this.service.sendPlaybackToClients(request);
    }

    @Override
    @PostMapping("/get-next-votes")
    public List<SongSPTF> getNextVotes(@RequestBody StringDTO token){

        return this.service.getNextVotes(token.getText());
    }

}
