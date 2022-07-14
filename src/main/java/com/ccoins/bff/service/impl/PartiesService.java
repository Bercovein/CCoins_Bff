package com.ccoins.bff.service.impl;

import com.ccoins.bff.dto.bars.BarTableDTO;
import com.ccoins.bff.dto.prizes.PartyDTO;
import com.ccoins.bff.dto.users.ClientDTO;
import com.ccoins.bff.exceptions.BadRequestException;
import com.ccoins.bff.exceptions.constant.ExceptionConstant;
import com.ccoins.bff.feign.PrizeFeign;
import com.ccoins.bff.service.IPartiesService;
import com.ccoins.bff.service.IRandomNameService;
import com.ccoins.bff.service.ITablesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class PartiesService extends ContextService implements IPartiesService {

    private final PrizeFeign prizeFeign;

    private final ITablesService tablesService;

    private final IRandomNameService randomizer;

    @Autowired
    public PartiesService(PrizeFeign prizeFeign, ITablesService tablesService, IRandomNameService randomizer) {
        this.prizeFeign = prizeFeign;
        this.tablesService = tablesService;
        this.randomizer = randomizer;
    }


    @Override
    public Long asignOrCreatePartyByCode(String code, ClientDTO clientDTO) {

        PartyDTO party;
        Optional<PartyDTO> partyOpt;

        //hay party activa en la mesa?
            //traer mesa por codigo y party por id de mesa
        BarTableDTO barTableDTO = this.tablesService.findByCode(code);

        partyOpt = this.findActivePartyByTable(barTableDTO.getId());

        if(partyOpt.isEmpty()){ //no -> crear party
            party = this.createParty(barTableDTO.getId());
        }else{
            party = partyOpt.get();
        }

        this.asignClientToParty(party.getId(),clientDTO.getId());

        return party.getId();
    }

    @Override
    public void asignClientToParty(Long partyId, Long clientId){

        try {
            this.prizeFeign.asignClientToParty(partyId, clientId);
        }catch (Exception e){
            throw new BadRequestException(ExceptionConstant.ADD_CLIENT_TO_PARTY_ERROR_CODE,
                    this.getClass(), ExceptionConstant.ADD_CLIENT_TO_PARTY_ERROR);
        }
    }

    @Override
    public PartyDTO createParty(Long tableId) {
        try {
            return this.prizeFeign.createParty(PartyDTO.builder()
                    .table(tableId)
                    .name(this.randomizer.randomGroupName())
                    .build());

        }catch (Exception e){
            throw new BadRequestException(ExceptionConstant.CREATE_PARTY_ERROR_CODE,
                    this.getClass(), ExceptionConstant.CREATE_PARTY_ERROR);
        }
    }


    @Override
    public Optional<PartyDTO> findActivePartyByTable(Long id){

        try {
            return this.prizeFeign.findActivePartyByTable(id);
        }catch (Exception e){
            throw new BadRequestException(ExceptionConstant.PARTY_BY_BAR_ERROR_CODE,
                    this.getClass(), ExceptionConstant.PARTY_BY_BAR_ERROR);
        }
    }
}
