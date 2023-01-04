package com.ccoins.bff.modal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class SongSPTF extends IdSPTF{

    @JsonProperty("track_number")
    private Integer trackNumber;
    private AlbumSPTF album;
    private List<ArtistSPTF> artists;

    @Builder
    public SongSPTF(String id, String name, String uri, Integer trackNumber, AlbumSPTF album, List<ArtistSPTF> artists) {
        super(id, name, uri);
        this.trackNumber = trackNumber;
        this.album = album;
        this.artists = artists;
    }
}
