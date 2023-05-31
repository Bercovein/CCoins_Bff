package com.ccoins.bff.controller;

import com.ccoins.bff.controller.swagger.IRedeemsController;
import com.ccoins.bff.dto.GenericRsDTO;
import com.ccoins.bff.dto.StringDTO;
import com.ccoins.bff.dto.coins.CoinsDTO;
import com.ccoins.bff.dto.coins.RedeemCodeRqDTO;
import com.ccoins.bff.service.ICodesService;
import com.ccoins.bff.utils.HeaderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redeem")
public class RedeemsController implements IRedeemsController {

    private final ICodesService service;

    @Autowired
    public RedeemsController(ICodesService service) {
        this.service = service;
    }

    @PostMapping("/code")
    @Override
    public ResponseEntity<GenericRsDTO<CoinsDTO>> redeemCode(@RequestBody StringDTO request, @RequestHeader HttpHeaders headers){

        RedeemCodeRqDTO redeemCode = RedeemCodeRqDTO.builder()
                .code(request.getText())
                .clientIp(HeaderUtils.getClient(headers))
                .partyId(HeaderUtils.getPartyId(headers))
                .build();

        return this.service.redeemCode(redeemCode);
    }
}
