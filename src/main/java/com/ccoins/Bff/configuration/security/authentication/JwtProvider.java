package com.ccoins.Bff.configuration.security.authentication;

import com.ccoins.Bff.configuration.security.JwtUserDTO;
import com.ccoins.Bff.configuration.security.JwtUtils;
import com.ccoins.Bff.configuration.security.PrincipalUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

import static com.ccoins.Bff.configuration.security.JwtUtils.SECRET_KEY;

@Component
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;

    public String generateToken(Authentication authentication, JwtUserDTO user){
        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();

        return Jwts.builder().setSubject(principalUser.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setClaims(JwtUtils.parse(user))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS512.getJcaName()))
                .compact();
    }

}
