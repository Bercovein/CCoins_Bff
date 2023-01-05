package com.ccoins.bff.service.impl;

import com.ccoins.bff.configuration.CredentialsSPTFConfig;
import com.ccoins.bff.exceptions.UnauthorizedException;
import com.ccoins.bff.exceptions.constant.ExceptionConstant;
import com.ccoins.bff.feign.SpotifyFeign;
import com.ccoins.bff.service.IServerSentEventService;
import com.ccoins.bff.service.ISpotifyService;
import com.ccoins.bff.spotify.sto.*;
import com.ccoins.bff.utils.MapperUtils;
import com.ccoins.bff.utils.enums.EventNamesEnum;
import feign.FeignException;
import org.modelmapper.internal.util.CopyOnWriteLinkedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SpotifyService implements ISpotifyService {

    private final SpotifyFeign feign;
    private final CredentialsSPTFConfig credentials;

    private final IServerSentEventService sseService;

    protected CopyOnWriteLinkedHashMap<Long, TokenPlaybackSPTF> actualSongs = new CopyOnWriteLinkedHashMap<>();


    @Autowired
    public SpotifyService(SpotifyFeign feign, CredentialsSPTFConfig credentials, IServerSentEventService sseService) {
        this.feign = feign;
        this.credentials = credentials;
        this.sseService = sseService;
    }

    @Override
    public ResponseEntity<PlaylistSPTF> getPlaylist(HttpHeaders headers) {

        this.setParameters(headers);
        PlaylistSPTF response =  this.feign.getQueue(headers);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<RecentlyPlayedSPTF> getRecentlyPlayed(HttpHeaders headers, Integer limit) {

        this.setParameters(headers);
        RecentlyPlayedSPTF response =  this.feign.getRecentlyPlayed(headers,limit);
        return ResponseEntity.ok(response);
    }

    @Override
    public void addTrackToQueue(HttpHeaders headers, UriSPTF trackUri) {

        this.setParameters(headers);
        this.feign.addTrackToQueue(headers,trackUri);
    }

    @Override
    public CredentialsSPTFDTO getCredentials(){

        return (CredentialsSPTFDTO) MapperUtils.map(this.credentials, CredentialsSPTFDTO.class);
    }

    private void setParameters(HttpHeaders headers){
        headers.set("Accept-Encoding", "identity");
        headers.remove("content-length");
    }

    @Override
    public void addActualSongToList(Long barId, String token, PlaybackSPTF playback){

        Optional<PlaybackSPTF> optional = Optional.ofNullable(playback);
        TokenPlaybackSPTF tokenPlayback = TokenPlaybackSPTF.builder().token(token).build();

        if(optional.isPresent()) {
            tokenPlayback.setPlayback(optional.get());
            this.actualSongs.put(barId, tokenPlayback);
        }else{
            this.actualSongs.replace(barId, tokenPlayback);
        }

        this.sseService.dispatchEventToClients(EventNamesEnum.ACTUAL_SONG_SPTF.name(),playback, barId);
    }

    @Override
    public void addTokenPlaybackInMemory(BarTokenDTO request){

        Long id = request.getId();
        try {
            if (!this.actualSongs.containsKey(id)) {
                this.addActualSongToList(id,
                        request.getToken(),
                        request.getPlayback()
                );
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
}
