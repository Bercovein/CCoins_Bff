package com.ccoins.bff.service;

import com.ccoins.bff.dto.TokenDTO;
import com.ccoins.bff.exceptions.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface IOauthService {

    ResponseEntity<?> google(TokenDTO request) throws CustomException;

    ResponseEntity<?> facebook(TokenDTO request) throws CustomException;

    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

}
