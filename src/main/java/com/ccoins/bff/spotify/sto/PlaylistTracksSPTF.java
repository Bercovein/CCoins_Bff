package com.ccoins.bff.spotify.sto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class PlaylistTracksSPTF {

    private String name;
    private TracksSPTF tracks;
}
