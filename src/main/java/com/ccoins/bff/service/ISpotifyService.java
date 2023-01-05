package com.ccoins.bff.service;

import com.ccoins.bff.spotify.sto.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface ISpotifyService {

    ResponseEntity<PlaylistSPTF> getPlaylist(HttpHeaders headers);

    ResponseEntity<RecentlyPlayedSPTF> getRecentlyPlayed(HttpHeaders headers, Integer limit);

    void addTrackToQueue(HttpHeaders headers, UriSPTF trackUri);

    CredentialsSPTFDTO getCredentials();

    void addActualSongToList(Long barId, String token, PlaybackSPTF playback);

    void addTokenPlaybackInMemory(BarTokenDTO request);

    PlaybackSPTF getPlaybackByBarId(Long id);

}
