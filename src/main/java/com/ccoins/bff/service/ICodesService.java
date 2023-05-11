package com.ccoins.bff.service;

import com.ccoins.bff.dto.GenericRsDTO;
import com.ccoins.bff.dto.StringDTO;
import com.ccoins.bff.dto.coins.CodeRqDTO;
import com.ccoins.bff.dto.coins.CoinsDTO;
import com.ccoins.bff.dto.coins.RedeemCodeRqDTO;
import com.ccoins.bff.spotify.sto.CodeDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ICodesService {

    ResponseEntity<List<CodeDTO>> createCodeByGameBarId(CodeRqDTO request);

    ResponseEntity<CodeDTO> ínvalidateCode(StringDTO request);

    ResponseEntity<List<CodeDTO>> getByActive(String state);

    ResponseEntity<GenericRsDTO<CoinsDTO>> redeemCode(RedeemCodeRqDTO request);
}