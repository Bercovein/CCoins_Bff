package com.ccoins.bff.spotify.sto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class PlaybackItemSPTF extends IdSPTF{

    @JsonProperty("duration_ms")
    private Long durationMs;

    @JsonProperty("track_number")
    private Long trackNumber;

    private AlbumSPTF album;

    private List<ArtistSPTF> artists;

}
