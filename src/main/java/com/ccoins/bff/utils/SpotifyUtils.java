package com.ccoins.bff.utils;

public class SpotifyUtils {

    public static String getUriId(String uri){
        String[] list = uri.split(":");
        return list[list.length-1]; //toma el id del uri
    }
}