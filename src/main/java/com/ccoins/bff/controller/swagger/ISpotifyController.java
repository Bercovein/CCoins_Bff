package com.ccoins.bff.controller.swagger;

import com.ccoins.bff.dto.StringDTO;
import com.ccoins.bff.spotify.sto.BarTokenDTO;
import com.ccoins.bff.spotify.sto.CodeDTO;
import com.ccoins.bff.spotify.sto.CredentialsSPTFDTO;
import com.ccoins.bff.spotify.sto.SongSPTF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "SPOTIFY")
public interface ISpotifyController {

    @ApiOperation(value = "Start to broadcast playable songs")
    void startPlayback(@RequestBody @Valid CodeDTO request);

    @ApiOperation(value = "Return config to authorize Spotify")
    ResponseEntity<CredentialsSPTFDTO> getCredentials();

    @ApiOperation(value = "Broadcast playable songs by pooling")
    void sendPlaybackToClients(@RequestBody @Valid BarTokenDTO request);

    @ApiOperation(value = "Get next song votes")
    List<SongSPTF> getNextVotes(@RequestBody StringDTO token);

    @ApiOperation(value = "Add voted song to next playback")
    void addVotedSongToNextPlayback(@Param("token") String token, @Param("context") String context, @Param("song") String song);

    @ApiOperation(value = "Returns if spotify is connected")
    ResponseEntity<Boolean> isConnected();

    @ApiOperation(value = "Disconnect my Spotify account from this app.")
    void disconnectByOwnerId();
}
