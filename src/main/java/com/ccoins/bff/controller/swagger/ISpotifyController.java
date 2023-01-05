package com.ccoins.bff.controller.swagger;

import com.ccoins.bff.spotify.sto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Api(tags = "SPOTIFY")
public interface ISpotifyController {

    @ApiOperation(value = "Get Playlist from Spotify")
    ResponseEntity<PlaylistSPTF> getPlaylist(HttpHeaders headers);

    @ApiOperation(value = "Get recent played list from Spotify")
    ResponseEntity<RecentlyPlayedSPTF> getRecentlyPlayed(@RequestHeader HttpHeaders headers, @PathVariable Integer limit);

    @ApiOperation(value = "Adds track to current list from Spotify")
    void addTrackToPlaylist(@RequestHeader HttpHeaders headers, @RequestBody UriSPTF request);

    @ApiOperation(value = "Return config to authorize Spotify")
    ResponseEntity<CredentialsSPTFDTO> getCredentials();

    @GetMapping("/playback")
    ResponseEntity<Optional<PlaybackSPTF>> getPlaybackState(@RequestHeader HttpHeaders headers);

    @PostMapping("/actualSongs")
    void addBarTokenToActualSongs(@RequestBody @Valid BarTokenDTO request);
}
