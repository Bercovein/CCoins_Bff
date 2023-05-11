package com.ccoins.bff.controller;

import com.ccoins.bff.dto.GenericRsDTO;
import com.ccoins.bff.dto.StringDTO;
import com.ccoins.bff.dto.coins.CodeRqDTO;
import com.ccoins.bff.dto.coins.CoinsDTO;
import com.ccoins.bff.dto.coins.RedeemCodeRqDTO;
import com.ccoins.bff.service.ICodesService;
import com.ccoins.bff.spotify.sto.CodeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/codes")
public class CodesController {

    @Autowired
    private ICodesService service;

    @PostMapping
    public ResponseEntity<List<CodeDTO>> createCodeByGameBarId(@RequestBody @Valid CodeRqDTO request){
        return this.service.createCodeByGameBarId(request);
    }

    @PutMapping("/invalidate")
    public ResponseEntity<CodeDTO> invalidateCode(@RequestBody StringDTO request){
        return this.service.ínvalidateCode(request);
    }

    @GetMapping("/game/{state}")
    public ResponseEntity<List<CodeDTO>> getByActive(@PathVariable("state") String state){
        return this.service.getByActive(state);
    }

    @PostMapping("/redeem")
    public ResponseEntity<GenericRsDTO<CoinsDTO>> redeemCode(@RequestBody @Valid RedeemCodeRqDTO request){
        return this.service.redeemCode(request);
    }
}
