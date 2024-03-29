package com.ccoins.bff.service;

import com.ccoins.bff.dto.coins.SongDTO;
import com.ccoins.bff.dto.coins.VotingDTO;
import com.ccoins.bff.spotify.sto.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface ISpotifyService {

    void generateToken(HttpHeaders headers, BarTokenDTO request);

    void refreshToken(HttpHeaders headers, BarTokenDTO request);

    BarTokenDTO getOrRefreshToken(BarTokenDTO request);

    ResponseEntity<PlaylistSPTF> getPlaylist(HttpHeaders headers);

    ResponseEntity<RecentlyPlayedSPTF> getRecentlyPlayed(HttpHeaders headers, Integer limit);

    void addTrackToQueue(HttpHeaders headers, List<String> tracks, int position, String playlistId);

    CredentialsSPTFDTO getCredentials();

    void resolveAndGenerateVotation(BarTokenDTO request, VotingDTO actualVoting);

    @Async
    SongDTO newWinner(Long barId, VotingDTO actualVoting);

    VotingDTO newVoting(String token, PlaybackSPTF playbackSPTF, Long barId);

    void startPlayback(CodeDTO request);

    PlaybackSPTF getPlayback(BarTokenDTO request);

    void sendPlaybackToClients(BarTokenDTO request);

    List<SongSPTF> getNextVotes(String token);

    void addVotedSongToNextPlayback(String token, PlaybackSPTF playbackSPTF, SongDTO winnerSong) ;

    void disconnectByOwnerId();

    ResponseEntity<Boolean> isConnected();
}
