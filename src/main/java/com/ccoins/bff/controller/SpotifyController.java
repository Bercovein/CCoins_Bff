package com.ccoins.bff.controller;

import com.ccoins.bff.controller.swagger.ISpotifyController;
import com.ccoins.bff.dto.StringDTO;
import com.ccoins.bff.dto.coins.SongDTO;
import com.ccoins.bff.service.ISpotifyService;
import com.ccoins.bff.spotify.sto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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

    @Override
    @GetMapping("/add-voted-song-to-next-playback")
    public void addVotedSongToNextPlayback(@Param("token") String token,@Param("context") String context,@Param("song")  String song){
        this.service.addVotedSongToNextPlayback(token, PlaybackSPTF.builder().context(UriSPTF.builder().uri(context).build()).build(), SongDTO.builder().uri(song).build());
    }

}
