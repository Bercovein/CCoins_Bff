package com.ccoins.bff.configuration.security.filter;

import com.ccoins.bff.configuration.security.JwtUtils;
import com.ccoins.bff.configuration.security.authentication.JwtProvider;
import com.ccoins.bff.service.impl.LoginService;
import com.ccoins.bff.exceptions.constant.ExceptionConstant;
import com.ccoins.bff.exceptions.utils.ErrorUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.ccoins.bff.configuration.security.JwtUtils.buildJwt;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
public class JwtRefreshFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private LoginService loginService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        ofNullable(request)
                .map(req -> req.getHeader(JwtUtils.AUTHORIZATION))   // obtenemos el token de la cabecera de la peticion
                .filter(token -> token.startsWith(JwtUtils.PREFIX))
                .ifPresent(token -> {
                    try {
                        Claims claims = JwtUtils.getClaims(token);

                        LocalDateTime expiration = claims.getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();     // obtenemos fecha de expiracion del token
                        LocalDateTime now = Instant.now().atZone(ZoneId.systemDefault()).toLocalDateTime();     // obtenemos fecha actual
                        long minToSubtract = (JwtUtils.EXPIRATION_TIME_SECONDS - 60) / 60;

                        if (now.isAfter(expiration.minus(minToSubtract, MINUTES))) {     // verficamos si paso 1 minuto desde la creacion del token

                            String newToken = buildJwt(claims);
                            response.addHeader(JwtUtils.AUTHORIZATION, JwtUtils.PREFIX + newToken);
                            response.setContentType(JwtUtils.APPLICATION_JSON_VALUE);
                        }
                    } catch(ExpiredJwtException e) {
                        response.setStatus(UNAUTHORIZED.value());
                        response.setContentType(JwtUtils.APPLICATION_JSON_VALUE);
                        try {
                            response.getWriter().write(new ObjectMapper()
                                    .writeValueAsString(ErrorUtils.buildMessage(ExceptionConstant.JWT_EXPIRED_ERROR_CODE, e.getMessage())));
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    } catch(Exception ignored) { }
                });

        if(response.getStatus() != UNAUTHORIZED.value())
            filterChain.doFilter(request, response);
    }

}
