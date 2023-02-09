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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
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
        this.feign.addTrackToQueue(headers,trackUri);
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

        this.sseService.dispatchEventToAllClientsFromBar(EventNamesEnum.ACTUAL_SONG_SPTF.name(), request.getPlayback(), barId);

        if(playbackSPTF != null && playbackSPTF.getItem() != null){

            if(playbackSPTF.isShuffleState()){ //quita el aleatorio de la lista
                this.changeShuffleState(token, false);
            }

            try {
                //valida si faltan 5 seg para que termine la canción y resuelve la votación
                if(playbackSPTF.getItem().getDurationMs() - playbackSPTF.getProgressMs() <= this.votesBeforeEndSongMs){

                    SongDTO winnerSong = this.newWinner(barId);

                    //si no hay playlist uri entonces no se crea una nueva votación
                    if(playbackSPTF.getContext() != null && !StringsUtils.isNullOrEmpty(playbackSPTF.getContext().getUri())){
                        this.addVotedSongToNextPlayback(token, playbackSPTF, winnerSong);
                        this.newVoting(token, playbackSPTF, barId);
                    }
                }
                //devuelve la votación actual
                this.getActualVotes(barId);

            }catch(Exception ignored){}
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
    public void newVoting(String token, PlaybackSPTF playbackSPTF, Long barId) {

        List<SongSPTF> list = this.getNextVotes(token); //ESTAS CANCIONES DEBERIAN VIAJAR A LA NUEVA VOTACIÓN
        this.voteService.createNewVoting(barId, list);
    }

    @Override
    @Async
    public void getActualVotes(Long barId){
        VotingDTO actualVoting = this.voteService.getActualVotingByBar(barId);
        if(actualVoting != null && !actualVoting.getSongs().isEmpty()) {
            this.sseService.dispatchEventToAllClientsFromBar(EventNamesEnum.ACTUAL_VOTES_SPTF.name(), actualVoting.getSongs(), barId);
        }
    }

    @Override
    public List<SongSPTF> getNextVotes(String token){

        HttpHeaders headers = HeaderUtils.getHeaderFromTokenWithEncodingAndWithoutContentLength(token);
        PlaylistSPTF playlistSPTF = this.feign.getQueue(headers); //trae la playlist actual
        List<SongSPTF> songs = playlistSPTF.getQueue();

        //toma los 3 temas siguientes
        if(songs.isEmpty() || songs.size() == 1)
            return new ArrayList<>();

        return songs.subList(1, Math.min(songs.size(), maxToVote + 1));
    }

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
        this.feign.addTrackToQueue(headers,trackUri);
    }
}
