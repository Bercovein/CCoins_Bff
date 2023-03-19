package com.ccoins.bff.service;

import com.ccoins.bff.dto.coins.SongDTO;
import com.ccoins.bff.dto.coins.VotingDTO;
import com.ccoins.bff.spotify.sto.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface ISpotifyService {

    ResponseEntity<PlaylistSPTF> getPlaylist(HttpHeaders headers);

    ResponseEntity<RecentlyPlayedSPTF> getRecentlyPlayed(HttpHeaders headers, Integer limit);

    void addTrackToQueue(HttpHeaders headers, UriSPTF trackUri);

    CredentialsSPTFDTO getCredentials();

    VotingDTO resolveAndGenerateVotation(BarTokenDTO request, VotingDTO actualVoting);

    void changeShuffleState(String token, boolean bool);

    @Async
    SongDTO newWinner(Long barId);

    VotingDTO newVoting(String token, PlaybackSPTF playbackSPTF, Long barId);

    void sendPlaybackToClients(BarTokenDTO request);

    List<SongSPTF> getNextVotes(String token);

    void addVotedSongToNextPlayback(String token, PlaybackSPTF playbackSPTF, SongDTO winnerSong);
}
