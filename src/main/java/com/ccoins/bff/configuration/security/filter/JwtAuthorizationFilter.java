package com.ccoins.bff.configuration.security.filter;

import com.ccoins.bff.configuration.security.JwtUtils;
import com.ccoins.bff.configuration.security.authentication.JwtAuthentication;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {  // filtro que se ejecuta cada vez que hagamos un request

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String token = request.getHeader(JwtUtils.AUTHORIZATION);    // obtenemos el token del parametro authorization del header

        if(JwtUtils.isRequiredAuthentication(token)){
            chain.doFilter(request, response);      // continua con la ejecucion de los demas filtros
            return;
        }
        Authentication authentication = null;

        if(JwtUtils.validateToken(token))
            authentication = getAuthentication(token);

        SecurityContextHolder.getContext().setAuthentication(authentication); // asignamos el objeto authentication dentro del contexto, esto autentica al usuario dentro del request

        if(authentication != null)
            chain.doFilter(request, response); // continuamos con la cadena de ejecucion del request, para los otros filtros y controladores
    }

    private Authentication getAuthentication(String token) throws IOException {

        Claims claims = JwtUtils.getClaims(token);
        String email = String.valueOf(claims.get(JwtUtils.EMAIL));
        Long userId = Long.valueOf(String.valueOf(claims.get(JwtUtils.ID)));


        return JwtAuthentication.builder()
                .id(userId)
//                .authorities(JwtUtils.getAuthorities(token))
                .principal(email)
//                .name(email)
                .build();
    }

}
