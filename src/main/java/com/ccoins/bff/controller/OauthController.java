package com.ccoins.bff.controller;

import com.ccoins.bff.controller.swagger.IOauthController;
import com.ccoins.bff.dto.TokenDTO;
import com.ccoins.bff.exceptions.CustomException;
import com.ccoins.bff.service.IOauthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
public class OauthController implements IOauthController {

    private final IOauthService oauthService;

    @Autowired
    public OauthController(IOauthService oauthService) {
        this.oauthService = oauthService;
    }

    @Override
    @PostMapping("/google")
    public ResponseEntity<?> google(@RequestBody TokenDTO request) throws CustomException {
        return this.oauthService.google(request);
    }

    @Override
    @PostMapping("/facebook")
    public ResponseEntity<?> facebook(@RequestBody TokenDTO request) throws CustomException{
        return this.oauthService.facebook(request);
    }
}
