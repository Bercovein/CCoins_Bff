package com.ccoins.Bff.configuration.security;

import com.ccoins.Bff.dto.users.OwnerDTO;
import com.ccoins.Bff.exceptions.ObjectNotFoundException;
import com.ccoins.Bff.exceptions.constant.ExceptionConstant;
import com.ccoins.Bff.utils.DateUtils;
import com.ccoins.Bff.utils.MapperUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Base64Utils;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class JwtUtils {

    public static final String PREFIX = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    public static final String LOCATION = "Location";
    public static final String AUTHORITIES = "authorities";
    public static final String BEARER_SPACE = "Bearer ";
    public static final String HEADER = "header";
    public static final String TOKEN = "token";
    public static final String APPLICATION_JSON_VALUE="application/json";
    public static final String VALID_JWT="Valid JWT ...";
    public static final String AUTHORITIES_TXT="authorities";
    public static final String EMPTY="";
    public static final String ROLES = "OWNER";
    public static final String JWT_ID = "12IDJWT";
    public static final String ESCAPED_POINT="\\.";
    public static final String POINT=".";
    public static final long EXPIRATION_TIME_SECONDS = 3600L;

    public static final String ID = "id";
    public static final String EMAIL = "email";


    public static final String SECRET_KEY = Base64Utils
            .encodeToString((";`.LY\"r7A:hb9a^Fm9]K6[jYd;Vei7Yuu+V~.`R~,Ww|SM?@?rZh4x{0qgiQ#6i" +
                    ",=\\HQajs6/ztWnU%0->tMd-Cp(?Ka<0OS_=cPAx1D-Wd/VxrF'Sa'EtM>0ZQ+`?").getBytes());

    public static final String TOKEN_SUBJECT = "sub";
    public static final String TOKEN_EMAIL = "email";

    public static String get(HttpHeaders headers, String variable){

        String value;

        if (headers != null) {

            value = headers.getFirst(variable);

            if(value == null) {
                throw new ObjectNotFoundException(ExceptionConstant.TOKEN_VARIABLE_NOT_FOUND_ERROR_CODE,
                        JwtUtils.class,
                        ExceptionConstant.TOKEN_VARIABLE_NOT_FOUND_ERROR
                );
            }
        }else{
            throw new ObjectNotFoundException(ExceptionConstant.TOKEN_NOT_FOUND_ERROR_CODE,
                    JwtUtils.class,
                    ExceptionConstant.TOKEN_NOT_FOUND_ERROR
            );
        }
        return value;
    }

    public static Long getId(HttpHeaders headers){
        return Long.valueOf(JwtUtils.get(headers,JwtUtils.ID));
    }

    public static Claims getClaims(String token) {     // claims contiene los datos, roles

        return Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes()) // asignamos la clave secreta
                .parseClaimsJws(token.replace(PREFIX, ""))  // quitamos el Bearer del token y obtenemos los datos
                .getBody();
    }

    public static String buildJwt(Claims claims) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(claims.getSubject())
                .signWith(new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS512.getJcaName()))
                .setIssuedAt(DateUtils.nowDate())
                .setExpiration(DateUtils.nowPlusDate(EXPIRATION_TIME_SECONDS * 1000))
                .compact();
    }


    public static boolean isRequiredAuthentication(String token) {

        return token == null || !token.startsWith(PREFIX);
    }

    public static boolean validateToken(String token) {

        try {
            getClaims(token);  // si no lanza ninguna expecion es valido
            return true;
        } catch(JwtException | IllegalArgumentException e){
            return false;
        }
    }

    public static Collection<? extends GrantedAuthority> getAuthorities(String token) throws IOException {

        Object roles = getClaims(token).get("authorities");     //  authorities = nombre que le dimos a nuestros roles en el claim en el metodo setClaims

        return Arrays.asList(new ObjectMapper()
                .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixIn.class)      // addMixIn= convertimos los roles a la clase SimpleGrantedAuthority
                .readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class));      // convertimos los roles en una collection de authorities. roles es de tipo object por lo que debemos pasarlo a json como string
    }

    public static Claims parse(JwtUserDTO user){

        ObjectMapper mapper = new ObjectMapper();

        Map<String, String> mappedUser =
                mapper.convertValue(user, Map.class);

        Claims claims = Jwts.claims();
        claims.putAll(mappedUser);

        return claims;
    }

    public static JwtUserDTO parse(OwnerDTO ownerDTO){
        return (JwtUserDTO) MapperUtils.map(ownerDTO, JwtUserDTO.class);
    }

}

