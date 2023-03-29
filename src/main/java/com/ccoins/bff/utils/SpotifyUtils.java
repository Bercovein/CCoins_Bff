package com.ccoins.bff.utils;

import com.ccoins.bff.utils.enums.SpotifyTypesEnum;

public class SpotifyUtils {

    public static final String REPEAT_STATE = "context";

    public static String getUriId(String uri){
        String[] list = uri.split(":");
        return list[list.length-1]; //toma el id del uri
    }

    public static boolean isPlaylist(String type){
        return SpotifyUtils.isType(type, SpotifyTypesEnum.PLAYLIST);
    }

    public static boolean isType(String type, SpotifyTypesEnum enumType){
        return enumType.getValue().equals(type);
    }
}
