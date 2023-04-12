package com.ccoins.bff.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class TokenUtils {

    private TokenUtils() {}

    public static boolean isJWTExpired(String jwtString) {

        try {
            DecodedJWT decodedJWT = JWT.decode(jwtString);
            Date expiresAt = decodedJWT.getExpiresAt();
            return expiresAt.before(new Date());
        } catch (JWTDecodeException e) {
            return true;
        }
    }
}
