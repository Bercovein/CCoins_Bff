package com.ccoins.bff.controller;

import com.ccoins.bff.controller.swagger.ICodesController;
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
public class CodesController implements ICodesController {

    private final ICodesService service;

    @Autowired
    public CodesController(ICodesService service) {
        this.service = service;
    }

    @PostMapping
    @Override
    public ResponseEntity<List<CodeDTO>> createCodeByGameBarId(@RequestBody @Valid CodeRqDTO request){
        return this.service.createCodeByGameBarId(request);
    }

    @PutMapping("/invalidate")
    @Override
    public ResponseEntity<CodeDTO> invalidateCode(@RequestBody StringDTO request){
        return this.service.invalidateCode(request);
    }

    @GetMapping("/game/{state}")
    @Override
    public ResponseEntity<List<CodeDTO>> getByActive(@PathVariable("state") String state){
        return this.service.getByActive(state);
    }

    @PostMapping("/redeem")
    @Override
    public ResponseEntity<GenericRsDTO<CoinsDTO>> redeemCode(@RequestBody @Valid RedeemCodeRqDTO request){
        return this.service.redeemCode(request);
    }
}
