package com.ccoins.bff.utils;

import org.springframework.http.HttpHeaders;

public class HeaderUtils {

    public static final String CLIENT = "client";
    public static final String CODE = "code";

    public static String get(HttpHeaders headers, String variable){
        return headers.getFirst(variable);
    }

    public static String getClient(HttpHeaders headers){
        return HeaderUtils.get(headers, CLIENT);
    }

    public static String getCode(HttpHeaders headers){
        return HeaderUtils.get(headers, CODE);
    }
}
