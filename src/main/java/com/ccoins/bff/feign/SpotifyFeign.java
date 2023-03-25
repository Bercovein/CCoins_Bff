package com.ccoins.bff.feign;

import com.ccoins.bff.dto.EmptyDTO;
import com.ccoins.bff.spotify.sto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "${spotify.feign.name}", url = "${spotify.feign.url}")
@RequestMapping("${spotify.feign.req-map}")
public interface SpotifyFeign {

    @GetMapping(value = "${spotify.path.get-queue}", produces = "application/json", consumes = "application/json")
    PlaylistSPTF getQueue(@RequestHeader HttpHeaders headers);

    @GetMapping(value = "${spotify.path.get-playlist}", produces = "application/json", consumes = "application/json")
    PlaylistTracksSPTF getPlaylist(@RequestHeader HttpHeaders headers, @RequestParam("playlist_id") String playlistId);

    @GetMapping(value = "${spotify.path.get-recently-played}", produces = "application/json", consumes = "application/json")
    RecentlyPlayedSPTF getRecentlyPlayed(@RequestHeader HttpHeaders headers, @PathVariable Integer limit);

    @PostMapping(value = "${spotify.path.add-items-to-playlist}", produces = "application/json", consumes = "application/json")
    void addItemsToPlaylist(@RequestHeader HttpHeaders headers,
                            @RequestParam("playlistId") String uri,
                            @RequestBody ItemToPlaylistSPTF request);

    @DeleteMapping(value = "${spotify.path.remove-song-from-playlist}", produces = "application/json", consumes = "application/json")
    void removeSongFromPlaylist(@RequestHeader HttpHeaders headers, @RequestParam("playlistId") String playlistId, @RequestBody TrackUriListSPTF request);

    @PutMapping(value = "${spotify.path.change-shuffle-state}", produces = "application/json", consumes = "application/json")
    void changeShuffleState(@RequestHeader HttpHeaders headers, @PathVariable("state") boolean bool, @RequestBody EmptyDTO request);
}
