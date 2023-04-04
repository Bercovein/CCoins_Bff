package com.ccoins.bff.spotify.sto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BarTokenDTO {

    @NotNull
    private Long id;
    @NotEmpty
    private String token;

    private String refreshToken;

    private PlaybackSPTF playback;

    private List<SongSPTF> toVote;
}
