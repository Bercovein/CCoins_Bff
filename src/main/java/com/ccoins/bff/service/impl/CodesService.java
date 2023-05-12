package com.ccoins.bff.service.impl;

import com.ccoins.bff.dto.GenericRsDTO;
import com.ccoins.bff.dto.StringDTO;
import com.ccoins.bff.dto.bars.GameDTO;
import com.ccoins.bff.dto.coins.CodeRqDTO;
import com.ccoins.bff.dto.coins.CoinsDTO;
import com.ccoins.bff.dto.coins.RedeemCodeRqDTO;
import com.ccoins.bff.dto.prizes.ClientPartyDTO;
import com.ccoins.bff.exceptions.BadRequestException;
import com.ccoins.bff.feign.BarsFeign;
import com.ccoins.bff.feign.CoinsFeign;
import com.ccoins.bff.feign.PrizeFeign;
import com.ccoins.bff.service.ICodesService;
import com.ccoins.bff.service.IServerSentEventService;
import com.ccoins.bff.spotify.sto.CodeDTO;
import com.ccoins.bff.utils.RegexUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ccoins.bff.exceptions.constant.ExceptionConstant.*;
import static com.ccoins.bff.utils.enums.EventNamesCoinsEnum.NEW_PRIZE;
import static com.ccoins.bff.utils.enums.EventNamesCoinsEnum.UPDATE_COINS;

@Service
public class CodesService extends ContextService implements ICodesService {

    private final CoinsFeign coinsFeign;

    private final PrizeFeign  prizeFeign;

    private final IServerSentEventService sseService;

    @Autowired
    public CodesService(CoinsFeign coinsFeign, IServerSentEventService sseService, BarsFeign barsFeign, PrizeFeign prizeFeign) {
        super(barsFeign);
        this.coinsFeign = coinsFeign;
        this.sseService = sseService;
        this.prizeFeign = prizeFeign;
    }

    @Override
    public ResponseEntity<List<CodeDTO>> createCodeByGameBarId(CodeRqDTO request){

        ResponseEntity<GameDTO> gameRE;

        if(RegexUtils.validateRegexAtoZMiddleDash(request.getCode())){
            throw new BadRequestException(WRONG_REGEX_CODE_ERROR_CODE, this.getClass(), WRONG_REGEX_CODE_ERROR);
        }

        try {
            gameRE = super.barsFeign.findCodeGameByBarId(super.getLoggedUserId());

            if(!gameRE.hasBody() || gameRE.getBody() == null){
                throw new BadRequestException();
            }
        }catch(Exception e){
            throw new BadRequestException(GET_CODE_GAME_ERROR_CODE,this.getClass(), GET_CODE_GAME_ERROR);
        }

        GameDTO game = gameRE.getBody();

        request.setGame(game.getId());

        return this.coinsFeign.createCodeByGameBarId(request);

    }

    @Override
    public ResponseEntity<CodeDTO> ínvalidateCode(StringDTO request) {

        if(RegexUtils.validateRegexAtoZMiddleDash(request.getText())){
            throw new BadRequestException(WRONG_REGEX_CODE_ERROR_CODE, this.getClass(), WRONG_REGEX_CODE_ERROR);
        }

        return this.coinsFeign.invalidateCode(request);
    }

    @Override
    public ResponseEntity<List<CodeDTO>> getByActive(String state) {

        ResponseEntity<GameDTO> id = this.barsFeign.findCodeGameByBarId(super.getLoggedUserId());

        if(!id.hasBody()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
        }

        return this.coinsFeign.getByActive(id.getBody().getId(),state);
    }

    @Override
    public ResponseEntity<GenericRsDTO<CoinsDTO>> redeemCode(RedeemCodeRqDTO request) {

        //busca al lider si no se envió por la request
        this.searchLeader(request);

        if(RegexUtils.validateRegexAtoZMiddleDash(request.getCode())){
            throw new BadRequestException(WRONG_REGEX_CODE_ERROR_CODE, this.getClass(), WRONG_REGEX_CODE_ERROR);
        }
        ResponseEntity<GenericRsDTO<CoinsDTO>> response = this.coinsFeign.redeemCode(request);

        if(response.hasBody() && response.getBody() != null && response.getBody().getCode() == null && response.getBody().getData() != null){

            if(response.getBody().getData().getQuantity() != null) {
                this.sseService.dispatchEventToClientsFromParty(UPDATE_COINS.name(), Strings.EMPTY, request.getPartyId());
            }

            if(response.getBody().getData().getQuantity() == null || response.getBody().getData().getQuantity() == 0){
                this.sseService.dispatchEventToClientsFromParty(NEW_PRIZE.name(), Strings.EMPTY,request.getPartyId());
            }
        }

        return response;
    }

    public void  searchLeader(RedeemCodeRqDTO request){
        if(request.getClientId() == null){
            List<ClientPartyDTO> clients = this.prizeFeign.findClientsByPartyId(request.getPartyId());

            Optional<ClientPartyDTO> leaderOpt = clients.stream().filter(ClientPartyDTO::isLeader).findFirst();

            if(leaderOpt.isPresent()){
                request.setClientId(leaderOpt.get().getClient());
            }else{
                request.setClientId(clients.stream().findAny().get().getClient());
            }
        }
    }
}
