package com.ccoins.bff.service;

import com.ccoins.bff.dto.coins.SongDTO;
import com.ccoins.bff.spotify.sto.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;

public interface ISpotifyService {

    ResponseEntity<PlaylistSPTF> getPlaylist(HttpHeaders headers);

    ResponseEntity<RecentlyPlayedSPTF> getRecentlyPlayed(HttpHeaders headers, Integer limit);

    void addTrackToQueue(HttpHeaders headers, UriSPTF trackUri);

    CredentialsSPTFDTO getCredentials();

    void changeShuffleState(String token, boolean bool);

    @Async
    SongDTO newWinner(Long barId);

    @Async
    void newVoting(String token, PlaybackSPTF playbackSPTF, Long barId);

    @Async
    void getActualVotes(Long barId);

    void sendPlaybackToClients(BarTokenDTO request);

}
