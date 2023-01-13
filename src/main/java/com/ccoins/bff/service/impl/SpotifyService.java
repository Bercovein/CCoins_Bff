package com.ccoins.bff.service.impl;

import com.ccoins.bff.configuration.CredentialsSPTFConfig;
import com.ccoins.bff.exceptions.UnauthorizedException;
import com.ccoins.bff.exceptions.constant.ExceptionConstant;
import com.ccoins.bff.feign.SpotifyFeign;
import com.ccoins.bff.service.IServerSentEventService;
import com.ccoins.bff.service.ISpotifyService;
import com.ccoins.bff.spotify.sto.*;
import com.ccoins.bff.utils.HeaderUtils;
import com.ccoins.bff.utils.MapperUtils;
import com.ccoins.bff.utils.enums.EventNamesEnum;
import feign.FeignException;
import org.modelmapper.internal.util.CopyOnWriteLinkedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SpotifyService implements ISpotifyService {

    private final SpotifyFeign feign;
    private final CredentialsSPTFConfig credentials;

    private final IServerSentEventService sseService;

    @Value("${spotify.max-to-vote}")
    private int maxToVote;

    @Value("${spotify.vote-before-ms}")
    private int votesBeforeEndSongMs;

    protected CopyOnWriteLinkedHashMap<Long, TokenPlaybackSPTF> actualSongs = new CopyOnWriteLinkedHashMap<>();


    @Autowired
    public SpotifyService(SpotifyFeign feign, CredentialsSPTFConfig credentials, IServerSentEventService sseService) {
        this.feign = feign;
        this.credentials = credentials;
        this.sseService = sseService;
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

        return (CredentialsSPTFDTO) MapperUtils.map(this.credentials, CredentialsSPTFDTO.class);
    }


    @Override
    @Async
    public void addActualSongToList(Long barId, String token, PlaybackSPTF playback){

        Optional<PlaybackSPTF> optional = Optional.ofNullable(playback);
        TokenPlaybackSPTF tokenPlayback = TokenPlaybackSPTF.builder().token(token).build();

        if(optional.isPresent()) {
            tokenPlayback.setPlayback(optional.get());
            this.actualSongs.put(barId, tokenPlayback);
        }else{
            this.actualSongs.replace(barId, tokenPlayback);
        }
    }

    @Override
    public void addTokenPlaybackInMemory(BarTokenDTO request){

        Long id = request.getId();
        try {
            PlaybackSPTF playbackSPTF = request.getPlayback();
            String token = request.getToken();
            if (!this.actualSongs.containsKey(id)) {
                this.addActualSongToList(id,
                        token,
                        playbackSPTF
                );

                if(playbackSPTF != null
                        && playbackSPTF.getItem() != null
                        && (playbackSPTF.getItem().getDurationMs() - playbackSPTF.getProgressMs() <= this.votesBeforeEndSongMs)){

                    //resolver resultado de la votación
                    this.addVotedSongToNextPlayback(token, playbackSPTF);
                    List<SongSPTF> list = this.getNextVotes(token); //ESTAS CANCIONES DEBERIAN VIAJAR A LA NUEVA VOTACIÓN
                }

                this.sseService.dispatchEventToClients(EventNamesEnum.ACTUAL_SONG_SPTF.name(), request.getPlayback(), id);
            }
        }catch(FeignException e){
            throw new UnauthorizedException(ExceptionConstant.SPOTIFY_PLAYBACK_ERROR_CODE,
                    this.getClass(),
                    e.getLocalizedMessage());
        }
    }

    @Override
    public PlaybackSPTF getPlaybackByBarId(Long id){

        TokenPlaybackSPTF tokenPlayback = this.actualSongs.get(id);
        PlaybackSPTF playback = null;

        if(tokenPlayback != null){
            playback = tokenPlayback.getPlayback();
        }
        return playback;
    }

    public List<SongSPTF> getNextVotes(String token){

        HttpHeaders headers = HeaderUtils.getHeaderFromTokenWithEncodingAndWithoutContentLength(token);
        PlaylistSPTF playlistSPTF = this.feign.getQueue(headers); //trae la playlist actual
        List<SongSPTF> songs = playlistSPTF.getQueue();

        //toma los 3 temas siguientes
        if(songs.isEmpty() || songs.size() == 1)
            return new ArrayList<>();

        return songs.subList(0, Math.min(songs.size(), maxToVote));
    }

    public void addVotedSongToNextPlayback(String token, PlaybackSPTF playbackSPTF){

        String songUri = null; // BUSCAR LA CANCIÓN GANADORA DE LA VOTACIÓN

        HttpHeaders headers = HeaderUtils.getHeaderFromTokenWithEncodingAndWithoutContentLength(token);

        UriSPTF playlistUri = playbackSPTF.getContext();
        String[] list = playlistUri.getUri().split(":");
        String playlistId = list[list.length-1]; //toma el id del uri de la playlist

        UriSPTF trackUri = UriSPTF.builder().uri(songUri).build();

        //quitar la canción de la lista
        this.feign.removeSongFromPlaylist(headers,
                playlistId,
                TrackUriListSPTF.builder().tracks(List.of(trackUri)).build()
        );

        //agregarla a continuación
        this.feign.addTrackToQueue(headers,trackUri);
    }
}
