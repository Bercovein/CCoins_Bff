package com.ccoins.bff.spotify.sto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class TracksSPTF {

    private List<ItemSPTF> items;
}
