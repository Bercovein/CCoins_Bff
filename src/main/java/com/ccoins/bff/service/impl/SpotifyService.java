package com.ccoins.bff.service.impl;

import com.ccoins.bff.configuration.CredentialsSPTFConfig;
import com.ccoins.bff.dto.EmptyDTO;
import com.ccoins.bff.dto.coins.SongDTO;
import com.ccoins.bff.dto.coins.VotingDTO;
import com.ccoins.bff.feign.SpotifyFeign;
import com.ccoins.bff.service.IServerSentEventService;
import com.ccoins.bff.service.ISpotifyService;
import com.ccoins.bff.service.IVoteService;
import com.ccoins.bff.spotify.sto.*;
import com.ccoins.bff.utils.HeaderUtils;
import com.ccoins.bff.utils.MapperUtils;
import com.ccoins.bff.utils.StringsUtils;
import com.ccoins.bff.utils.enums.EventNamesEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class SpotifyService implements ISpotifyService {

    private final SpotifyFeign feign;
    private final CredentialsSPTFConfig credentials;
    private final IServerSentEventService sseService;
    private final IVoteService voteService;

    @Value("${spotify.max-to-vote}")
    private int maxToVote;

    @Value("${spotify.vote-before-ms}")
    private int votesBeforeEndSongMs;

    @Autowired
    public SpotifyService(SpotifyFeign feign, CredentialsSPTFConfig credentials, IServerSentEventService sseService, IVoteService voteService) {
        this.feign = feign;
        this.credentials = credentials;
        this.sseService = sseService;
        this.voteService = voteService;
    }

    @Override
    public ResponseEntity<PlaylistSPTF> getPlaylist(HttpHeaders headers) {

        HeaderUtils.setParameters(headers);
        PlaylistSPTF response =  this.feign.getQueue(headers);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<RecentlyPlayedSPTF> getRecentlyPlayed(HttpHeaders headers, Integer limit) {

        HeaderUtils.setParameters(headers);
        RecentlyPlayedSPTF response =  this.feign.getRecentlyPlayed(headers,limit);
        return ResponseEntity.ok(response);
    }

    @Override
    public void addTrackToQueue(HttpHeaders headers, UriSPTF trackUri) {

        HeaderUtils.setParameters(headers);
        this.feign.addTrackToQueue(headers,trackUri.getUri(), EmptyDTO.builder().build());
    }

    @Override
    public CredentialsSPTFDTO getCredentials(){
        return MapperUtils.map(this.credentials, CredentialsSPTFDTO.class);
    }

    @Override
    public void sendPlaybackToClients(BarTokenDTO request){

        Long barId = request.getId();

        PlaybackSPTF playbackSPTF = request.getPlayback();
        String token = request.getToken();

        //envia a todos los usuarios el estado de la canción actual (asi se esté reproduciendo o no)
        this.sseService.dispatchEventToAllClientsFromBar(EventNamesEnum.ACTUAL_SONG_SPTF.name(), playbackSPTF, barId);

        //solo si la canción existe
        if(playbackSPTF != null && playbackSPTF.getItem() != null){

            //quita el modo aleatorio de la lista si es que lo tiene
            if(playbackSPTF.isShuffleState()){
                this.changeShuffleState(token, false);
            }

            try {
                //toma la votación actual del bar
                VotingDTO actualVoting = this.voteService.getActualVotingByBar(barId);

                //si la votación no existe, crea una nueva
                if(actualVoting == null){
                    actualVoting = this.newVoting(token, playbackSPTF, barId);
                }

                if(playbackSPTF.getItem().getDurationMs() - playbackSPTF.getProgressMs() <= this.votesBeforeEndSongMs){

                    SongDTO winnerSong = this.newWinner(barId);

                    //si no hay playlist uri entonces no se crea una nueva votación
                    if(playbackSPTF.getContext() != null && !StringsUtils.isNullOrEmpty(playbackSPTF.getContext().getUri())){
                        this.addVotedSongToNextPlayback(token, playbackSPTF, winnerSong);
                        this.newVoting(token, playbackSPTF, barId);
                    }
                }
                //devuelve la votación actual
                if(actualVoting != null && !actualVoting.getSongs().isEmpty()) {
                    this.sseService.dispatchEventToAllClientsFromBar(EventNamesEnum.ACTUAL_VOTES_SPTF.name(), actualVoting.getSongs(), barId);
                }

            }catch(Exception e){
                log.error(e.getMessage());
            }
        }
    }

    @Override
    public void changeShuffleState(String token, boolean bool){

        try {
            HttpHeaders headers = HeaderUtils.getHeaderFromTokenWithEncodingAndWithoutContentLength(token);
            EmptyDTO request = EmptyDTO.builder().build();
            this.feign.changeShuffleState(headers, bool, request);
        }catch (Exception ignored){}
    }

    @Override
    public SongDTO newWinner(Long barId) {

        //resolver la votación
        VotingDTO voting = this.voteService.resolveVoting(barId);
        SongDTO winnerSong = null;

        if (voting != null) {
            winnerSong = voting.getWinnerSong();
            this.sseService.dispatchEventToAllClientsFromBar(EventNamesEnum.NEW_WINNER_SPTF.name(), voting.getWinnerSong(), barId);

            List<String> clientIpList = this.voteService.giveSongCoinsByGame(barId, voting);
            this.sseService.dispatchEventToSomeClientsFromBar(EventNamesEnum.YOU_WIN_SONG_VOTE_SPTF.name(), null, barId, clientIpList);
        }

        return winnerSong;
    }

    @Override
    public VotingDTO newVoting(String token, PlaybackSPTF playbackSPTF, Long barId) {

        List<SongSPTF> list = this.getNextVotes(token); //ESTAS CANCIONES DEBERIAN VIAJAR A LA NUEVA VOTACIÓN
        return this.voteService.createNewVoting(barId, list);
    }

    @Override
    public List<SongSPTF> getNextVotes(String token){

        HttpHeaders headers = HeaderUtils.getHeaderFromTokenWithEncodingAndWithoutContentLength(token);
        PlaylistSPTF playlistSPTF = this.feign.getQueue(headers); //trae la playlist actual
        List<SongSPTF> songs = playlistSPTF.getQueue();

        if(songs.isEmpty() || songs.size() == 1)
            return new ArrayList<>();

        Random rand = new Random();
        songs.get(rand.nextInt(songs.size()));

        return songs.subList(0, Math.min(songs.size(), maxToVote));
    }

    @Override
    public void addVotedSongToNextPlayback(String token, PlaybackSPTF playbackSPTF, SongDTO winnerSong){

        if(winnerSong == null || StringsUtils.isNullOrEmpty(winnerSong.getUri())){
            return;
        }

        HttpHeaders headers = HeaderUtils.getHeaderFromTokenWithEncodingAndWithoutContentLength(token);

        UriSPTF playlistUri = playbackSPTF.getContext();
        String[] list = playlistUri.getUri().split(":");
        String playlistId = list[list.length-1]; //toma el id del uri de la playlist

        UriSPTF trackUri = UriSPTF.builder().uri(winnerSong.getUri()).build();

        //quitar la canción de la lista
        this.feign.removeSongFromPlaylist(headers,
                playlistId,
                TrackUriListSPTF.builder().tracks(List.of(trackUri)).build()
        );

        //agregarla a continuación
        this.addTrackToQueue(headers,trackUri);
    }
}
