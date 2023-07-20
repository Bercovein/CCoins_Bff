package com.ccoins.bff.configuration.security.authentication;

import com.ccoins.bff.configuration.security.JwtUserDTO;
import com.ccoins.bff.configuration.security.JwtUtils;
import com.ccoins.bff.configuration.security.PrincipalUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.ccoins.bff.configuration.security.JwtUtils.SECRET_KEY;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;

    public String generateToken(Authentication authentication, JwtUserDTO user){
        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();


        return Jwts.builder()
                .setSubject(principalUser.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setClaims(JwtUtils.parse(user))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

}
