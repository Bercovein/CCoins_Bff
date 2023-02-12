package com.ccoins.bff.service.impl;

import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.ListDTO;
import com.ccoins.bff.dto.bars.GameDTO;
import com.ccoins.bff.exceptions.BadRequestException;
import com.ccoins.bff.exceptions.constant.ExceptionConstant;
import com.ccoins.bff.feign.BarsFeign;
import com.ccoins.bff.service.IGamesService;
import com.ccoins.bff.utils.HeaderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GamesService extends ContextService implements IGamesService {

    private final BarsFeign barsFeign;


    @Autowired
    public GamesService(BarsFeign barsFeign) {
        this.barsFeign = barsFeign;
    }

    @Override
    public ResponseEntity<GameDTO> saveOrUpdate(GameDTO request) {

        try{
            return this.barsFeign.saveOrUpdateGame(request);
        }catch(Exception e){
            throw new BadRequestException(ExceptionConstant.GAME_CREATE_OR_UPDATE_ERROR_CODE,
                    this.getClass(), ExceptionConstant.GAME_CREATE_OR_UPDATE_ERROR);
        }
    }

    @Override
    public ResponseEntity<GameDTO> findById(IdDTO id) {

        GameDTO game;

        try{
            game = this.barsFeign.findGameById(id.getId()).getBody();
        }catch(Exception e){
            throw new BadRequestException(ExceptionConstant.GAME_FIND_BY_ID_ERROR_CODE,
                    this.getClass(), ExceptionConstant.GAME_FIND_BY_ID_ERROR);
        }

        return ResponseEntity.ok(game);
    }

    @Override
    public ResponseEntity<ListDTO> findAllByBar(Long id) {

        try{
            return this.barsFeign.findAllGamesByBar(id);
        }catch(Exception e){
            throw new BadRequestException(ExceptionConstant.GAME_FIND_BY_OWNER_ERROR_CODE,
                    this.getClass(), ExceptionConstant.GAME_FIND_BY_OWNER_ERROR);
        }
    }

    @Override
    public ResponseEntity<GameDTO> active(IdDTO request) {
        try{
            return this.barsFeign.activeGame(request.getId());
        }catch(Exception e){
            throw new BadRequestException(ExceptionConstant.GAME_ACTIVE_ERROR_CODE,
                    this.getClass(), ExceptionConstant.GAME_ACTIVE_ERROR);
        }
    }

    @Override
    public ResponseEntity<ListDTO> findAllGamesTypes() {

        try{
            return this.barsFeign.findAllGamesTypes();
        }catch(Exception e){
            throw new BadRequestException(ExceptionConstant.GAME_FIND_GAME_TYPES_ERROR_CODE,
                    this.getClass(), ExceptionConstant.GAME_FIND_GAME_TYPES_ERROR);
        }
    }

    @Override
    public ResponseEntity<ListDTO> findGamesByBarToClients(HttpHeaders headers) {

        try{
            IdDTO barId = this.barsFeign.getBarIdByParty(HeaderUtils.getPartyId(headers)).getBody();

            return this.barsFeign.findAllActiveByBar(barId.getId());
        }catch(Exception e){
            throw new BadRequestException(ExceptionConstant.GAME_FIND_BY_CLIENT_ERROR_CODE,
                    this.getClass(), ExceptionConstant.GAME_FIND_BY_CLIENT_ERROR);
        }

    }
}
