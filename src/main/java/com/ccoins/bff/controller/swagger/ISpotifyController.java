package com.ccoins.bff.controller.swagger;

import com.ccoins.bff.spotify.sto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.validation.Valid;

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

    @PostMapping("/actualSongs")
    void addBarTokenToActualSongs(@RequestBody @Valid BarTokenDTO request);

}
