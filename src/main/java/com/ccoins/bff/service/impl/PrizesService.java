package com.ccoins.bff.service.impl;

import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.ListDTO;
import com.ccoins.bff.dto.ResponseDTO;
import com.ccoins.bff.dto.bars.BarTableDTO;
import com.ccoins.bff.dto.prizes.PartyDTO;
import com.ccoins.bff.dto.prizes.PrizeDTO;
import com.ccoins.bff.dto.users.ClientDTO;
import com.ccoins.bff.exceptions.BadRequestException;
import com.ccoins.bff.exceptions.ObjectNotFoundException;
import com.ccoins.bff.exceptions.constant.ExceptionConstant;
import com.ccoins.bff.feign.PrizeFeign;
import com.ccoins.bff.service.ICoinsService;
import com.ccoins.bff.service.IPrizesService;
import com.ccoins.bff.utils.DateUtils;
import com.ccoins.bff.utils.HeaderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrizesService extends ContextService implements IPrizesService {

    private final PrizeFeign prizeFeign;

    private final ClientService clientService;

    private final PartiesService partiesService;

    private final TablesService tablesService;

    private final ICoinsService coinsService;

    @Autowired
    public PrizesService(PrizeFeign prizeFeign, ClientService clientService, PartiesService partiesService, TablesService tablesService, ICoinsService coinsService) {
        this.prizeFeign = prizeFeign;
        this.clientService = clientService;
        this.partiesService = partiesService;
        this.tablesService = tablesService;
        this.coinsService = coinsService;
    }

    @Override
    public ResponseEntity<PrizeDTO> saveOrUpdate(PrizeDTO request) {

        try{
            return this.prizeFeign.saveOrUpdatePrize(request);
        }catch(Exception e){
            throw new BadRequestException(ExceptionConstant.PRIZE_CREATE_OR_UPDATE_ERROR_CODE,
                    this.getClass(), ExceptionConstant.PRIZE_CREATE_OR_UPDATE_ERROR);
        }
    }

    @Override
    public ResponseEntity<PrizeDTO> findById(IdDTO request) {

        PrizeDTO prize;

        try{
            prize = this.prizeFeign.findPrizeById(request.getId()).getBody();
        }catch(Exception e){
            throw new BadRequestException(ExceptionConstant.PRIZE_FIND_BY_ID_ERROR_CODE,
                    this.getClass(), ExceptionConstant.PRIZE_FIND_BY_ID_ERROR);
        }

        if(prize == null){
            throw new ObjectNotFoundException(ExceptionConstant.PRIZE_NOT_FOUND_ERROR_CODE,
                    this.getClass(), ExceptionConstant.PRIZE_NOT_FOUND_ERROR);
        }

        return ResponseEntity.ok(prize);
    }

    @Override
    public ResponseEntity<ListDTO> findAllByBar(IdDTO request, Optional<String> status) {

        try{
            if(status.isPresent()) {
                return this.prizeFeign.findAllPrizesByBarAndStatus(request.getId(),status.get());
            } else {
                return this.prizeFeign.findAllPrizesByBar(request.getId());
            }
        }catch(Exception e){
            throw new BadRequestException(ExceptionConstant.PRIZE_FIND_BY_BAR_ERROR_CODE,
                    this.getClass(), ExceptionConstant.PRIZE_FIND_BY_BAR_ERROR);
        }
    }

    @Override
    public ResponseEntity<PrizeDTO> active(IdDTO request) {
        try{
            return this.prizeFeign.activePrize(request.getId());
        }catch(Exception e){
            throw new BadRequestException(ExceptionConstant.PRIZE_UPDATE_ACTIVE_ERROR_CODE,
                    this.getClass(), ExceptionConstant.PRIZE_UPDATE_ACTIVE_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> buyPrizeByTableAndUser(IdDTO idDTO, String client, String code) {

        //busca el premio
        PrizeDTO prize = this.findById(idDTO).getBody();

        assert prize != null;
        if(prize.getEndDate() != null && DateUtils.isAfterLocalDateTime(DateUtils.nowLocalDateTime(),prize.getEndDate())){
            throw new BadRequestException(ExceptionConstant.PRIZE_UNAVAILABLE_ERROR_CODE,
                    this.getClass(), ExceptionConstant.PRIZE_UNAVAILABLE_ERROR);
        }

        //busca al cliente por IP
        ClientDTO clientDTO = this.clientService.findActiveByIp(client);

        //busca la party activa por codigo
        PartyDTO partyOpt = this.partiesService.findActivePartyByTableCode(code);

        return this.coinsService.spendCoinsInPrizeByParty(partyOpt.getId(),clientDTO.getId(), prize.getId(), prize.getPoints());
    }

    @Override
    public ResponseEntity<ListDTO> findAllByHeader(HttpHeaders headers) {

        BarTableDTO barTableDTO = this.tablesService.findByCode(HeaderUtils.getCode(headers));
        return this.findAllByBar(IdDTO.builder().id(barTableDTO.getBar()).build(), Optional.of("active"));
    }

}
