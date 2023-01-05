package com.ccoins.bff.spotify.sto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ImagesSPTF {

    private String url;
    private Integer height;
    private Integer width;
}
