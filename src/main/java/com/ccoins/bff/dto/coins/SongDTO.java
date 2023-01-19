package com.ccoins.bff.dto.coins;

import com.ccoins.bff.spotify.sto.SongSPTF;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SongDTO {

    private Long id;
    private String name;
    private Long votes;

    public static SongDTO convert(SongSPTF song){

        return SongDTO.builder()
                .votes(0L)
                .name(song.getName() + ", " + song.getArtists().get(0).getName())
                .build();
    }
}
