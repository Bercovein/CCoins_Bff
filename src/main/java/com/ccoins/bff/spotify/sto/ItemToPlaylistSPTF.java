package com.ccoins.bff.spotify.sto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class ItemToPlaylistSPTF {

    private int position;
    private List<String> uris;

    @Builder
    public ItemToPlaylistSPTF(int position, List<String> uris) {
        this.position = position;
        this.uris = uris;
    }
}
