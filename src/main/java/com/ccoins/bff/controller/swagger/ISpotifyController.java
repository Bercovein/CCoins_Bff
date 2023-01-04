package com.ccoins.bff.controller.swagger;

import com.ccoins.bff.modal.PlaylistSPTF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import static com.ccoins.bff.controller.swagger.SwaggerConstants.SPOTIFY;

@Api(tags = SPOTIFY)
public interface ISpotifyController {

    @ApiOperation(value = "Get Playlist from Spotify")
    ResponseEntity<PlaylistSPTF> getPlaylist(HttpHeaders headers);
}
