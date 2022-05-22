package com.ccoins.Bff.utils;

import com.ccoins.Bff.exceptions.ObjectNotFoundException;
import com.ccoins.Bff.exceptions.constant.ExceptionConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpHeaders;

import java.io.IOException;

public class TokenUtils {

    public static final String PREFIX = "Bearer";
    public static final String AUTHORIZATION = "Authorization";
    public static final String AUTHORITIES = "authorities";
    public static final String BEARER_SPACE = "Bearer ";
    public static final String APPLICATION_JSON_VALUE="application/json";
    public static final String VALID_JWT="Valid JWT ...";
    public static final String AUTHORITIES_TXT="authorities";
    public static final String EMPTY="";
    public static final String ROLES = "OWNER";
    public static final String JWT_ID = "12IDJWT";
    public static final String ESCAPED_POINT="\\.";
    public static final String POINT=".";

    public static final String TOKEN_SUBJECT = "sub";
    public static final String TOKEN_EMAIL = "email";

    public static String get(HttpHeaders headers, String variable){

        if (headers == null) {
            throw new ObjectNotFoundException(
                    ExceptionConstant.TOKEN_VARIABLE_NOT_FOUND_ERROR_CODE,
                    TokenUtils.class,
                    ExceptionConstant.TOKEN_VARIABLE_NOT_FOUND_ERROR
            );
        }
        return headers.getFirst(variable);
    }

    public Claims getClaims(String data)throws IOException {
        try {
            String token = data.replace(PREFIX, EMPTY);
            String[] jwsTokenParts = token.split(ESCAPED_POINT);
            String jwtToken = jwsTokenParts[0] + POINT + jwsTokenParts[1] + POINT;
            return Jwts.parser().parseClaimsJwt(jwtToken).getBody();
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

}

