package com.ccoins.Bff.configuration.security.service;

import com.ccoins.Bff.configuration.security.PrincipalUser;
import com.ccoins.Bff.configuration.security.authentication.JwtProvider;
import com.ccoins.Bff.dto.TokenDTO;
import com.ccoins.Bff.dto.users.OwnerDTO;
import com.ccoins.Bff.exceptions.CustomException;
import com.ccoins.Bff.exceptions.UnauthorizedException;
import com.ccoins.Bff.exceptions.constant.ExceptionConstant;
import com.ccoins.Bff.exceptions.utils.ErrorUtils;
import com.ccoins.Bff.service.IOauthService;
import com.ccoins.Bff.service.IUsersService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
public class OauthService implements IOauthService, UserDetailsService {

    private final String googleClientId;

    private final String secretPsw;

    private final IUsersService usersService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    @Autowired
    public OauthService(@Value("${google.client.id}") String googleClientId,
                        @Value("${secretPsw}") String secretPsw,
                        IUsersService usersService,
                        PasswordEncoder passwordEncoder,
                        AuthenticationManager authenticationManager,
                        JwtProvider jwtProvider) {
        this.googleClientId = googleClientId;
        this.secretPsw = secretPsw;
        this.usersService = usersService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public ResponseEntity<?> google(TokenDTO request) throws CustomException {

        final NetHttpTransport transport = new NetHttpTransport();
        final JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();

        try{
            GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
                    .setAudience(Collections.singletonList(googleClientId));
            final GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(),request.getValue());
            GoogleIdToken.Payload payload = googleIdToken.getPayload();

            OwnerDTO ownerDTO = this.usersService.findOrCreateOwner(payload.getEmail());
            return ResponseEntity.ok(login(ownerDTO));
        }catch(Exception e){
            log.error(ErrorUtils.parseMethodError(this.getClass()));
            throw new UnauthorizedException(ExceptionConstant.GOOGLE_ERROR_CODE, this.getClass(), ExceptionConstant.GOOGLE_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> facebook(TokenDTO request) throws CustomException{

        Facebook facebook = new FacebookTemplate(request.getValue());
        final String[] fields = {"email", "picture"}; //agrega el email y la foto para devolver a front
        User user;

        try{
            user = facebook.fetchObject("me", User.class, fields);
            OwnerDTO ownerDTO = this.usersService.findOrCreateOwner(user.getEmail());
            return ResponseEntity.ok(login(ownerDTO));

        }catch(Exception e){
            log.error(ErrorUtils.parseMethodError(this.getClass()));
            throw new UnauthorizedException(ExceptionConstant.FACEBOOK_ERROR_CODE, this.getClass(), ExceptionConstant.FACEBOOK_ERROR);
        }
    }

    private TokenDTO login(OwnerDTO user){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), secretPsw)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        return TokenDTO.builder().value(jwt).build();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        try{
            OwnerDTO ownerDTO = this.usersService.findByEmail(email).get();

            return PrincipalUser.build(ownerDTO, passwordEncoder.encode(secretPsw));
        }catch(Exception e){
            log.error(ErrorUtils.parseMethodError(this.getClass()));
            throw new UnauthorizedException(ExceptionConstant.USER_NOT_FOUND_ERROR_CODE, this.getClass(), ExceptionConstant.USER_NOT_FOUND_ERROR);
        }
    }

}
