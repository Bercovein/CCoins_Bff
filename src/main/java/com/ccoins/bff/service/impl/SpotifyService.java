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
import com.ccoins.bff.utils.*;
import com.ccoins.bff.utils.enums.EventNamesSPTFEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.ccoins.bff.utils.SpotifyUtils.REPEAT_STATE;

@Service
@Slf4j
public class SpotifyService implements ISpotifyService {

    private final SpotifyFeign feign;
    private final CredentialsSPTFConfig credentials;
    private final IServerSentEventService sseService;
    private final IVoteService voteService;

    @Value("${spotify.max-to-vote}")
    private int maxToVote;

    @Value("${spotify.playback.track-url}")
    private String trackLink;

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
    public void addTrackToQueue(HttpHeaders headers, List<String> tracks, int position, String playlistId) {

        HeaderUtils.setParameters(headers);
        this.feign.addItemsToPlaylist(headers,playlistId, ItemToPlaylistSPTF.builder().position(position).uris(tracks).build());
    }

    @Override
    public CredentialsSPTFDTO getCredentials(){
        return MapperUtils.map(this.credentials, CredentialsSPTFDTO.class);
    }

    @Override
    @Async
    public void sendPlaybackToClients(BarTokenDTO request){

        Long barId = request.getId();

        PlaybackSPTF playbackSPTF = request.getPlayback();
        String token = request.getToken();

        if(playbackSPTF == null){
            return;
        }
        playbackSPTF.setSongLink(trackLink.concat(SpotifyUtils.getUriId(playbackSPTF.getItem().getUri())));

        //envia a todos los usuarios el estado de la canción actual (asi se esté reproduciendo o no)
        this.sseService.dispatchEventToAllClientsFromBar(EventNamesSPTFEnum.ACTUAL_SONG_SPTF.name(), playbackSPTF, barId);

        //solo si la canción existe
        //si no hay playlist uri entonces no se genera nada
        if(playbackSPTF.getItem() == null
            || playbackSPTF.getContext() == null
//            || !SpotifyUtils.isPlaylist(playbackSPTF.getContext().getType())
            || StringsUtils.isNullOrEmpty(playbackSPTF.getContext().getUri())){
            this.sseService.dispatchEventToAllClientsFromBar(EventNamesSPTFEnum.ACTUAL_VOTES_SPTF.name(), new ArrayList<>(), barId);
            return;
        }

        //quita el modo aleatorio de la lista si es que lo tiene
        this.changeShuffleState(token, playbackSPTF.isShuffleState());

        //obliga a dejar el modo de repetición de la playlist
        this.changeRepeatState(token, playbackSPTF.getRepeatState());

        if(!playbackSPTF.isPlaying()){
            return;
        }

        try {
            //toma la votación actual del bar
            VotingDTO actualVoting = this.voteService.getActualVotingByBar(barId);

            //si faltan mas segundos antes de que termine la canción, no se genera nueva votación
            if(actualVoting != null
                    && !DateUtils.isLowerFromNowThan(actualVoting.getMatch().getStartDate(), 60)
                    && request.getPlayback().getItem().getDurationMs() - request.getPlayback().getProgressMs() <= this.votesBeforeEndSongMs){
                this.resolveAndGenerateVotation(request, actualVoting);
                actualVoting = null;
            }

            //si la votación no existe, crea una nueva (caso de ser el primer tema en reproducción)
            if (actualVoting == null) {
                actualVoting = this.newVoting(token, playbackSPTF, barId);
            }

            //devuelve la votación actual
            if (actualVoting != null)
                this.sseService.dispatchEventToAllClientsFromBar(EventNamesSPTFEnum.ACTUAL_VOTES_SPTF.name(), actualVoting.getSongs(), barId);

        }catch(Exception e){
            log.error(e.getMessage());
        }

    }

    @Override
    public void resolveAndGenerateVotation(BarTokenDTO request, VotingDTO actualVoting){

        Long barId = request.getId();
        PlaybackSPTF playbackSPTF = request.getPlayback();
        String token = request.getToken();

        //genera un nuevo ganador
        SongDTO winnerSong = this.newWinner(barId, actualVoting);

        //añade la canción ganadora en la posición siguiente a reproducir
        this.addVotedSongToNextPlayback(token, playbackSPTF, winnerSong);
    }

    @Override
    public void changeShuffleState(String token, boolean bool){

        try {
            if(bool){
                HttpHeaders headers = HeaderUtils.getHeaderFromTokenWithEncodingAndWithoutContentLength(token);
                this.feign.changeShuffleState(headers, false, EmptyDTO.builder().build());
            }
        }catch (Exception ignored){}
    }

    @Override
    public void changeRepeatState(String token, String state){

        try {
            if(!REPEAT_STATE.equals(state)){
                HttpHeaders headers = HeaderUtils.getHeaderFromTokenWithEncodingAndWithoutContentLength(token);
                this.feign.changeRepeatState(headers, REPEAT_STATE, EmptyDTO.builder().build());
            }
        }catch (Exception ignored){}
    }

    @Override
    public SongDTO newWinner(Long barId, VotingDTO actualVoting) {

        //resolver la votación
        VotingDTO voting = this.voteService.resolveVoting(actualVoting);
        SongDTO winnerSong = null;

        if (voting != null) {
            winnerSong = voting.getWinnerSong();
            this.sseService.dispatchEventToAllClientsFromBar(EventNamesSPTFEnum.NEW_WINNER_SPTF.name(), voting.getWinnerSong(), barId);

            this.voteService.giveSongCoinsByGame(barId, voting);
        }

        return winnerSong;
    }

    @Override
    public VotingDTO newVoting(String token, PlaybackSPTF playbackSPTF, Long barId) {

        List<SongSPTF> list = this.getNextVotes(token); //ESTAS CANCIONES VIAJAN A LA NUEVA VOTACIÓN

        if(list.isEmpty()){
            return null;
        }

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
        List<SongSPTF> votingSongs = new ArrayList<>();

        for (int i=1; i <= Math.min(songs.size(), maxToVote); i++){
            votingSongs.add(songs.remove(rand.nextInt(songs.size())));
        }

        return votingSongs;
    }

    @Override
    @Async
    public void addVotedSongToNextPlayback(String token, PlaybackSPTF playbackSPTF, SongDTO winnerSong)  {

        if(winnerSong == null || StringsUtils.isNullOrEmpty(winnerSong.getUri())){
            return;
        }

        HttpHeaders headers = HeaderUtils.getHeaderFromTokenWithEncodingAndWithoutContentLength(token);

        UriSPTF playlistUri = playbackSPTF.getContext();
        String playlistId = SpotifyUtils.getUriId(playlistUri.getUri());

        UriSPTF trackUri = UriSPTF.builder().uri(winnerSong.getUri()).build();

        try {
            //trae la lista actual para validación
            PlaylistSPTF playlistSPTF = this.feign.getQueue(headers);
            List<SongSPTF> queue = playlistSPTF.getQueue();

            //checkea si es el proximo en reproducirse, si lo es o está vacía, no hace nada
            if (queue.isEmpty() || trackUri.getUri().equals(queue.get(0).getUri())) {
                return;
            }

            PlaylistTracksSPTF playlistTracks = this.feign.getPlaylist(headers,playlistId);
            List<ItemSPTF> tracks = playlistTracks.getTracks().getItems();
            Optional<ItemSPTF> actualSongOpt = tracks.stream().filter(t -> t.getTrack().getUri().equals(playbackSPTF.getItem().getUri())).findFirst();

            if(actualSongOpt.isPresent()) {

                int position = tracks.indexOf(actualSongOpt.get());
                position++;

                //quitar la canción de la lista
                this.feign.removeSongFromPlaylist(headers,
                        playlistId,
                        TrackUriListSPTF.builder().tracks(List.of(trackUri)).build()
                );

                //agregarla a continuación
                this.addTrackToQueue(headers, List.of(trackUri.getUri()), position, playlistId);
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
