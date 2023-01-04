package com.ccoins.bff.modal;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class AlbumSPTF extends IdSPTF{

    private List<ImagesSPTF> images;

    @Builder
    public AlbumSPTF(String id, String name, String uri, List<ImagesSPTF> images) {
        super(id, name, uri);
        this.images = images;
    }
}
