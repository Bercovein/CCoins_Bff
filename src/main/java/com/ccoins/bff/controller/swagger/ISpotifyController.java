package com.ccoins.bff.controller.swagger;

import com.ccoins.bff.dto.StringDTO;
import com.ccoins.bff.spotify.sto.BarTokenDTO;
import com.ccoins.bff.spotify.sto.CredentialsSPTFDTO;
import com.ccoins.bff.spotify.sto.SongSPTF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "SPOTIFY")
public interface ISpotifyController {

    @ApiOperation(value = "Return config to authorize Spotify")
    ResponseEntity<CredentialsSPTFDTO> getCredentials();

    @PostMapping("/actualSongs")
    void sendPlaybackToClients(@RequestBody @Valid BarTokenDTO request);

    @PostMapping("/get-next-votes")
    List<SongSPTF> getNextVotes(@RequestBody StringDTO token);
}
