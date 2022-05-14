package com.ccoins.Bff.service;

import com.ccoins.Bff.dto.TokenDTO;
import com.ccoins.Bff.exceptions.CustomException;
import org.springframework.http.ResponseEntity;

public interface IOauthService {

    ResponseEntity<?> google(TokenDTO request) throws CustomException;

    ResponseEntity<?> facebook(TokenDTO request) throws CustomException;
}
