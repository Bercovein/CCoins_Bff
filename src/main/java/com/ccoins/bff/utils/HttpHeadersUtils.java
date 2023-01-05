package com.ccoins.bff.utils;

import org.springframework.http.HttpHeaders;

public class HttpHeadersUtils {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    public static String getBearerFromToken(String token){
        return HttpHeadersUtils.BEARER.concat(token);
    }

    public static HttpHeaders getHeaderFromToken(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeadersUtils.AUTHORIZATION, HttpHeadersUtils.getBearerFromToken(token));
        return headers;
    }
}
