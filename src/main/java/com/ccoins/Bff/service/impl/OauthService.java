package com.ccoins.Bff.service.impl;

import com.ccoins.Bff.dto.TokenDTO;
import com.ccoins.Bff.exceptions.CustomException;
import com.ccoins.Bff.exceptions.UnauthorizedException;
import com.ccoins.Bff.service.IOauthService;
import com.ccoins.Bff.service.IUsersService;
import com.ccoins.Bff.utils.ErrorUtils;
import com.ccoins.Bff.utils.constant.ExceptionConstant;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
public class OauthService implements IOauthService {

    @Value("${google.client.id}")
    String googleClientId;

    @Autowired
    private IUsersService usersService;

    @Override
    public ResponseEntity<?> google(TokenDTO request) throws CustomException {

        final NetHttpTransport transport = new NetHttpTransport();
        final JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();

        try{
            GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
                    .setAudience(Collections.singletonList(googleClientId));
            final GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(),request.getValue());
            GoogleIdToken.Payload payload = googleIdToken.getPayload();

            //this.usersService.findOrCreateOwner(payload.getEmail());

            return ResponseEntity.ok(payload);
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

            //this.usersService.findOrCreateOwner(user.getEmail());

            return ResponseEntity.ok(user);
        }catch(Exception e){
            log.error(ErrorUtils.parseMethodError(this.getClass()));
            throw new UnauthorizedException(ExceptionConstant.FACEBOOK_ERROR_CODE, this.getClass(), ExceptionConstant.FACEBOOK_ERROR);
        }
    }
}
