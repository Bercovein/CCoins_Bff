package com.ccoins.bff.utils;

import org.springframework.http.HttpHeaders;

public class HeaderUtils {

    public static final String CLIENT = "client";
    public static final String PARTY_ID = "partyid";
    public static final String CODE = "code";
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    public static String getBearerFromToken(String token){
        return HeaderUtils.BEARER.concat(token);
    }

    public static HttpHeaders getHeaderFromTokenWithEncodingAndWithoutContentLength(String token){
        HttpHeaders headers = HeaderUtils.getHeaderFromToken(token);
        setParameters(headers);
        return headers;
    }

    public static HttpHeaders getHeaderFromToken(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.set(HeaderUtils.AUTHORIZATION, HeaderUtils.getBearerFromToken(token));
        return headers;
    }

    public static String get(HttpHeaders headers, String variable){
        return headers.getFirst(variable);
    }

    public static String getClient(HttpHeaders headers){
        return HeaderUtils.get(headers, CLIENT);
    }

    public static Long getPartyId(HttpHeaders headers){
        String id = HeaderUtils.get(headers, PARTY_ID);
        return Long.parseLong(id);
    }

    public static String getCode(HttpHeaders headers){
        return HeaderUtils.get(headers, CODE);
    }

    public static void setParameters(HttpHeaders headers){
        headers.set("Accept-Encoding", "identity");
        headers.remove("Content-Length");
    }
}
