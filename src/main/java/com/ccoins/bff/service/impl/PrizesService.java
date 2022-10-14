package com.ccoins.bff.service.impl;

import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.ListDTO;
import com.ccoins.bff.dto.prizes.PrizeDTO;
import com.ccoins.bff.dto.users.ClientDTO;
import com.ccoins.bff.exceptions.BadRequestException;
import com.ccoins.bff.exceptions.constant.ExceptionConstant;
import com.ccoins.bff.feign.PrizeFeign;
import com.ccoins.bff.service.IPrizesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class PrizesService extends ContextService implements IPrizesService {

    private final PrizeFeign prizeFeign;

    private final ClientService clientService;

    private final PartiesService partiesService;

    @Autowired
    public PrizesService(PrizeFeign prizeFeign, ClientService clientService, PartiesService partiesService) {
        this.prizeFeign = prizeFeign;
        this.clientService = clientService;
        this.partiesService = partiesService;
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

        return ResponseEntity.ok(prize);
    }

    @Override
    public ResponseEntity<ListDTO> findAllByBar(IdDTO request, Optional<String> status) {

        try{
            if(status.isPresent()) {
                return this.prizeFeign.findAllPrizesByBarAndStatus(request.getId(),status);
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
    public void buyPrizeByTableAndUser(IdDTO idDTO, String client, String code) {

        PrizeDTO prize = this.findById(idDTO).getBody();

        if(prize == null){
            throw new RuntimeException();
        }

        ClientDTO clientDTO = this.clientService.findActiveByIp(client);

        this.partiesService.findActivePartyByTableCode(code);

        //si la party no tiene puntos, error

    }

}
