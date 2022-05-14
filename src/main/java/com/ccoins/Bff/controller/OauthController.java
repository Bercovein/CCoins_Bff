package com.ccoins.Bff.controller;

import com.ccoins.Bff.dto.TokenDTO;
import com.ccoins.Bff.exceptions.CustomException;
import com.ccoins.Bff.service.IOauthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
public class OauthController {

    private final IOauthService oauthService;

    @Autowired
    public OauthController(IOauthService oauthService) {
        this.oauthService = oauthService;
    }

    @PostMapping("/google")
    public ResponseEntity<?> google(@RequestBody TokenDTO request) throws CustomException {
        return this.oauthService.google(request);
    }

    @PostMapping("/facebook")
    public ResponseEntity<?> facebook(@RequestBody TokenDTO request) throws CustomException{
        return this.oauthService.facebook(request);
    }
}
