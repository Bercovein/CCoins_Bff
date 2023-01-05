package com.ccoins.bff.spotify.sto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class PlaybackSPTF {

    @JsonProperty("progress_ms")
    private Long progressMs;

    @JsonProperty("is_playing")
    private boolean playing;

    private PlaybackItemSPTF item;
}
