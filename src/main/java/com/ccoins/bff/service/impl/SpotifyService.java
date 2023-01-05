package com.ccoins.bff.service.impl;

import com.ccoins.bff.configuration.CredentialsSPTFConfig;
import com.ccoins.bff.exceptions.UnauthorizedException;
import com.ccoins.bff.exceptions.constant.ExceptionConstant;
import com.ccoins.bff.feign.SpotifyFeign;
import com.ccoins.bff.service.ISpotifyService;
import com.ccoins.bff.spotify.sto.*;
import com.ccoins.bff.utils.HttpHeadersUtils;
import com.ccoins.bff.utils.MapperUtils;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.internal.util.CopyOnWriteLinkedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class SpotifyService implements ISpotifyService {

    private final SpotifyFeign feign;
    private final CredentialsSPTFConfig credentials;

    protected CopyOnWriteLinkedHashMap<Long, TokenPlaybackSPTF> actualSongs = new CopyOnWriteLinkedHashMap<>();


    @Autowired
    public SpotifyService(SpotifyFeign feign, CredentialsSPTFConfig credentials) {
        this.feign = feign;
        this.credentials = credentials;
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

    @Override
    public Optional<PlaybackSPTF> getPlaybackState(HttpHeaders headers){
        this.setParameters(headers);
        return this.feign.getPlayState(headers);
    }

    private void setParameters(HttpHeaders headers){
        headers.set("Accept-Encoding", "identity");
        headers.remove("content-length");
    }

    @Scheduled(fixedDelayString = "${spotify.playback.cron}")
    public void loadActualSongs(){ //actualiza el estado de las canciones de los bares cada X tiempo
        actualSongs.forEach((key,value) -> {
            try {
                this.addActualSongToList(key, value.getToken());
            }catch(Exception ignored){

            }
        });
    }

    public void addActualSongToList(Long id, String token){
        HttpHeaders headers = HttpHeadersUtils.getHeaderFromToken(token);
        this.setParameters(headers);
        Optional<PlaybackSPTF> optional;

        optional = this.feign.getPlayState(headers);

        TokenPlaybackSPTF tokenPlayback = TokenPlaybackSPTF.builder().token(token).build();

        if(optional.isPresent()) {
            tokenPlayback.setPlayback(optional.get());
            this.actualSongs.put(id, tokenPlayback);
        }else{
            this.actualSongs.replace(id, tokenPlayback);
        }
    }

    @Override
    public void addTokenPlaybackInMemory(Long barId, String token){

        try {
            if (!this.actualSongs.containsKey(barId))
                this.addActualSongToList(barId,token);
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
