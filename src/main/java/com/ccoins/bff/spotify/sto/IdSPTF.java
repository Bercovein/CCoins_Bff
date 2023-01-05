package com.ccoins.bff.spotify.sto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class IdSPTF {
    private String id;
    private String name;
    private String uri;
}
