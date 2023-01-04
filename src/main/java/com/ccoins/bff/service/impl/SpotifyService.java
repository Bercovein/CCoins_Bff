package com.ccoins.bff.service.impl;

import com.ccoins.bff.feign.SpotifyFeign;
import com.ccoins.bff.modal.PlaylistSPTF;
import com.ccoins.bff.service.ISpotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SpotifyService implements ISpotifyService {

    private final SpotifyFeign feign;

    @Autowired
    public SpotifyService(SpotifyFeign feign) {
        this.feign = feign;
    }

    @Override
    public ResponseEntity<PlaylistSPTF> getPlaylist(HttpHeaders headers) {
        return this.feign.getQueue(headers);
    }
}
