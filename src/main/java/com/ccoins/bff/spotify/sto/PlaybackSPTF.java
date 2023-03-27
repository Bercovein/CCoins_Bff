package com.ccoins.bff.spotify.sto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class PlaybackSPTF {

    @JsonProperty("progress_ms")
    private Long progressMs;

    @JsonProperty("is_playing")
    private boolean playing;

    private PlaybackItemSPTF item;

    private UriSPTF context;

    @JsonProperty("shuffle_state")
    private boolean shuffleState;

    @JsonProperty("repeat_state")
    private String repeatState;

    private String songLink;
}
