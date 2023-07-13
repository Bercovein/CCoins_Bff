package com.ccoins.bff.configuration;

import com.ccoins.bff.configuration.security.authentication.JwtEntryPoint;
import com.ccoins.bff.configuration.security.filter.JwtAuthorizationFilter;
import com.ccoins.bff.configuration.security.filter.JwtRefreshFilter;
import com.ccoins.bff.service.impl.OAuthService;
import com.google.api.client.http.HttpMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static com.ccoins.bff.configuration.security.JwtUtils.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@CrossOrigin
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    OAuthService loginService;

    @Autowired
    JwtEntryPoint jwtEntryPoint;

    @Value("${api.url.path}")
    private String API_PATH;

    @Value("${api.url.path.localhost}")
    private String API_LOCALHOST_PATH;

    @Value("${api.ngrok.bypass}")
    private String NGROK_BYPASS;

    @Bean
    JwtRefreshFilter jwtTokenFilter (){
        return new JwtRefreshFilter();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loginService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests().antMatchers("/oauth/**", "/actuator/**","/clients/**","/votes/**","/sse/**","/spotify/**","/redeem/**", "/parties/**","/coins/**").permitAll()
                .antMatchers("/","/my/docs", "/v2/swagger", "/swagger-ui/**", "/swagger-ui.html", "/v2/api-docs", "/swagger-resources/**", "/webjars/**", "/swagger.json").permitAll()      // rutas publicas, no requieren autenticación
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtEntryPoint)
                .and()
                .cors().configurationSource(corsConfigurationSource())  // configuramos cors para acceder a los endpoints desde los domininios especificados
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilter(new JwtAuthorizationFilter(authenticationManager()))
            .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(Arrays.asList(API_PATH, API_LOCALHOST_PATH));                   // agregamos nuestros dominios, ej: "http://localhost:4200"
        config.setAllowedMethods(Arrays.asList(HttpMethods.GET, HttpMethods.POST, HttpMethods.PUT, HttpMethods.PATCH, HttpMethods.DELETE, HttpMethods.OPTIONS)); // configuramos los verbos que vamos a permitir en el backend
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Arrays.asList(CONTENT_TYPE, AUTHORIZATION, LOCATION, CLIENT, CODE, PARTY_ID, NGROK_BYPASS));
        config.setExposedHeaders(Arrays.asList(ACCESS_CONTROL_EXPOSE_HEADERS, AUTHORIZATION));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);                            // configurar cors para todos nuestros endpoints

        return source;
    }
}

