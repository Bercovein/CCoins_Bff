package com.ccoins.bff.spotify.sto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TokenPlaybackSPTF {

    private String token;
    private PlaybackSPTF playback;
}
