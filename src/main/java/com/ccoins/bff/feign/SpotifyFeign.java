package com.ccoins.bff.feign;

import com.ccoins.bff.spotify.sto.PlaylistSPTF;
import com.ccoins.bff.spotify.sto.RecentlyPlayedSPTF;
import com.ccoins.bff.spotify.sto.TrackUriListSPTF;
import com.ccoins.bff.spotify.sto.UriSPTF;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "${spotify.feign.name}", url = "${spotify.feign.url}")
@RequestMapping("${spotify.feign.req-map}")
public interface SpotifyFeign {

    @GetMapping(value = "${spotify.path.get-queue}", produces = "application/json", consumes = "application/json")
    PlaylistSPTF getQueue(@RequestHeader HttpHeaders headers);

    @GetMapping(value = "${spotify.path.get-recently-played}", produces = "application/json", consumes = "application/json")
    RecentlyPlayedSPTF getRecentlyPlayed(@RequestHeader HttpHeaders headers, @PathVariable Integer limit);

    @PostMapping(value = "${spotify.path.add-track-to-queue}", produces = "application/json", consumes = "application/json")
    void addTrackToQueue(@RequestHeader HttpHeaders headers, @RequestBody UriSPTF trackUri);

    @DeleteMapping(value = "${spotify.path.remove-song-from-playlist}", produces = "application/json", consumes = "application/json")
    void removeSongFromPlaylist(@RequestHeader HttpHeaders headers, @RequestParam("playlistId") String playlistId, @RequestBody TrackUriListSPTF request);

    @PutMapping(value = "${spotify.path.change-shuffle-state}", produces = "application/json", consumes = "application/json")
    void changeShuffleState(@RequestHeader HttpHeaders headers, @RequestParam("shuffle") boolean bool);
}
