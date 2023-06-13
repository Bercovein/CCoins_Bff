package com.ccoins.bff.service.impl;

import com.ccoins.bff.dto.GenericRsDTO;
import com.ccoins.bff.dto.StringDTO;
import com.ccoins.bff.dto.bars.GameDTO;
import com.ccoins.bff.dto.coins.CodeRqDTO;
import com.ccoins.bff.dto.coins.CoinsDTO;
import com.ccoins.bff.dto.coins.RedeemCodeRqDTO;
import com.ccoins.bff.dto.prizes.ClientPartyDTO;
import com.ccoins.bff.dto.prizes.PrizeDTO;
import com.ccoins.bff.dto.users.ClientDTO;
import com.ccoins.bff.exceptions.BadRequestException;
import com.ccoins.bff.exceptions.ObjectNotFoundException;
import com.ccoins.bff.feign.BarsFeign;
import com.ccoins.bff.feign.CoinsFeign;
import com.ccoins.bff.feign.PrizeFeign;
import com.ccoins.bff.feign.UsersFeign;
import com.ccoins.bff.service.ICodesService;
import com.ccoins.bff.service.IServerSentEventService;
import com.ccoins.bff.spotify.sto.CodeDTO;
import com.ccoins.bff.utils.RegexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.ccoins.bff.exceptions.constant.ExceptionConstant.*;
import static com.ccoins.bff.utils.enums.EventNamesEnum.NEW_PRIZE;
import static com.ccoins.bff.utils.enums.EventNamesEnum.UPDATE_COINS;
import static com.ccoins.bff.utils.enums.UpdateCoinsMessagesEnum.REDEEM_COINS;

@Service
public class CodesService extends ContextService implements ICodesService {

    private final CoinsFeign coinsFeign;

    private final PrizeFeign  prizeFeign;

    private final UsersFeign usersFeign;

    private final IServerSentEventService sseService;

    @Autowired
    public CodesService(CoinsFeign coinsFeign, IServerSentEventService sseService, BarsFeign barsFeign, PrizeFeign prizeFeign, UsersFeign usersFeign) {
        super(barsFeign);
        this.coinsFeign = coinsFeign;
        this.sseService = sseService;
        this.prizeFeign = prizeFeign;
        this.usersFeign = usersFeign;
    }

    @Override
    public ResponseEntity<List<CodeDTO>> createCodeByGameBarId(CodeRqDTO request){

        ResponseEntity<GameDTO> gameRE;

        if(request.getCode() != null && RegexUtils.validateRegexAtoZMiddleDash(request.getCode())){
            throw new BadRequestException(WRONG_REGEX_CODE_ERROR_CODE, this.getClass(), WRONG_REGEX_CODE_ERROR);
        }

        try {
            gameRE = super.barsFeign.findCodeGameByBarId(super.findBarIdByOwner());

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
    public ResponseEntity<CodeDTO> invalidateCode(StringDTO request) {

        if(RegexUtils.validateRegexAtoZMiddleDash(request.getText())){
            throw new BadRequestException(WRONG_REGEX_CODE_ERROR_CODE, this.getClass(), WRONG_REGEX_CODE_ERROR);
        }

        return this.coinsFeign.invalidateCode(request);
    }

    @Override
    public ResponseEntity<List<CodeDTO>> getByActive(String state) {

        ResponseEntity<GameDTO> id = this.barsFeign.findCodeGameByBarId(super.findBarIdByOwner());

        if(!id.hasBody()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
        }

        ResponseEntity<List<CodeDTO>> response = this.coinsFeign.getByActive(id.getBody().getId(),state);

        Set<Long> prizeIds = new HashSet<>();

        List<CodeDTO> codes = response.getBody() != null ? response.getBody() : new ArrayList<>();

        codes.stream().filter(codeDTO -> codeDTO.getPrize() != null).forEach(code -> prizeIds.add(code.getPrize()));

        prizeIds.forEach(prizeId -> {
            ResponseEntity<PrizeDTO> prize = this.prizeFeign.findPrizeById(prizeId);
            codes.stream().filter(codeDTO -> Objects.equals(codeDTO.getPrize(), prizeId))
                    .forEach(codeDTO -> codeDTO.setPrizeName(prize.getBody() != null? prize.getBody().getName() : null));
        });

        return response;
    }

    @Override
    public ResponseEntity<GenericRsDTO<CoinsDTO>> redeemCode(RedeemCodeRqDTO request) {


        if(RegexUtils.validateRegexAtoZMiddleDash(request.getCode())){
            throw new BadRequestException(WRONG_REGEX_CODE_ERROR_CODE, this.getClass(), WRONG_REGEX_CODE_ERROR);
        }

        this.searchLeader(request);

        Optional<ClientDTO> client = this.usersFeign.findActiveByIp(request.getClientIp());

        if(client.isEmpty()){
            throw new ObjectNotFoundException(CLIENT_REDEEM_CODE_ERROR_CODE, this.getClass(), CLIENT_REDEEM_CODE_ERROR);
        }

        request.setClientId(client.get().getId());

        ResponseEntity<GenericRsDTO<CoinsDTO>> response = this.coinsFeign.redeemCode(request);

        if(response.hasBody() && response.getBody() != null && response.getBody().getCode() == null && response.getBody().getData() != null){
            Long quantity = response.getBody().getData().getQuantity();
            if(quantity != null && quantity != 0L) {
                String message = String.format(REDEEM_COINS.getMessage(), quantity.toString());
                this.sseService.dispatchEventToClientsFromParty(UPDATE_COINS.name(),message, request.getPartyId());
            }else if(response.getBody().getData().getPrize() != null){
                this.sseService.dispatchEventToClientsFromParty(NEW_PRIZE.name(), NEW_PRIZE.getMessage(),request.getPartyId());
            }
        }

        return response;
    }

    public void searchLeader(RedeemCodeRqDTO request){

        if(request.getClientIp() == null){
            List<ClientPartyDTO> clients = this.prizeFeign.findClientsByPartyId(request.getPartyId());

            Optional<ClientPartyDTO> leaderOpt = clients.stream().filter(ClientPartyDTO::isLeader).findFirst();

            if(leaderOpt.isPresent()){
                request.setClientId(leaderOpt.get().getClient());
                //setear ip?
            }else{
                request.setClientId(clients.stream().findAny().get().getClient());
            }

            List<ClientDTO> clientsList = this.usersFeign.findByIdIn(List.of(request.getClientId()));

            if(!clientsList.isEmpty()){
                request.setClientIp(clientsList.get(0).getIp());
            }
        }
    }
}
