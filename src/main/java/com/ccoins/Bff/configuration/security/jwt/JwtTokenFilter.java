package com.ccoins.Bff.configuration.security.jwt;

import com.ccoins.Bff.service.impl.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UsersService usersService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            String token = getToken(request);
            String email = jwtProvider.getEmailFromToken(token);
            UserDetails userDetails = usersService.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email, null,null);
            SecurityContextHolder.getContext().setAuthentication(auth);

        }catch (Exception e){
            log.error("fail en el metodo doFilter");
        }
        filterChain.doFilter(request,response);
    }

    private String getToken(HttpServletRequest request){

        String bearer = "Bearer ";
        String authorization = "Authorization";

        String authRequest = request.getHeader(authorization);
        if(authRequest != null && authRequest.startsWith(bearer)){
            return authRequest.replace(bearer, Strings.EMPTY);
        }else{
            return null;
        }
    }
}
