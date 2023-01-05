package com.ccoins.bff.spotify.sto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PlaylistSPTF {

    private List<SongSPTF> queue;

    @JsonProperty("currently_playing")
    private SongSPTF currentlyPlaying;
}