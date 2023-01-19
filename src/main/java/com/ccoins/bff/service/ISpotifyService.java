package com.ccoins.bff.service;

import com.ccoins.bff.spotify.sto.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;

public interface ISpotifyService {

    ResponseEntity<PlaylistSPTF> getPlaylist(HttpHeaders headers);

    ResponseEntity<RecentlyPlayedSPTF> getRecentlyPlayed(HttpHeaders headers, Integer limit);

    void addTrackToQueue(HttpHeaders headers, UriSPTF trackUri);

    CredentialsSPTFDTO getCredentials();

    @Async
    void newWinner(Long barId);

    @Async
    void newVoting(String token, PlaybackSPTF playbackSPTF, Long barId);

    @Async
    void getActualVotes(Long barId);

    void addActualSongToList(Long barId, String token, PlaybackSPTF playback);

    void addTokenPlaybackInMemory(BarTokenDTO request);

    PlaybackSPTF getPlaybackByBarId(Long id);

}
