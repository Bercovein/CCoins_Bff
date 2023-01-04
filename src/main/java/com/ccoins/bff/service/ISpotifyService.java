package com.ccoins.bff.service;

import com.ccoins.bff.modal.PlaylistSPTF;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface ISpotifyService {

    ResponseEntity<PlaylistSPTF> getPlaylist(HttpHeaders headers);
}
