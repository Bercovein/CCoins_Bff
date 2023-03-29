package com.ccoins.bff.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SpotifyTypesEnum {

    ARTIST("artist"),
    PLAYLIST("playlist"),
    ALBUM("album"),
    SHOW("show");

    final String value;
}
