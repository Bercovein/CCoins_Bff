package com.ccoins.bff.service.impl;

import com.ccoins.bff.dto.bars.BarTableDTO;
import com.ccoins.bff.dto.prizes.PartyDTO;
import com.ccoins.bff.dto.users.ClientDTO;
import com.ccoins.bff.feign.PrizeFeign;
import com.ccoins.bff.service.IPartiesService;
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

    @Autowired
    public PartiesService(PrizeFeign prizeFeign, ITablesService tablesService) {
        this.prizeFeign = prizeFeign;
        this.tablesService = tablesService;
    }


    @Override
    public void asignOrCreatePartyByCode(String code, ClientDTO clientDTO) {

        PartyDTO party;

        //hay party activa en la mesa?
            //traer mesa por codigo y party por id de mesa
        BarTableDTO barTableDTO = this.tablesService.findByCode(code);

        Optional<PartyDTO> partyOpt = this.prizeFeign.findActivePartyByTable(barTableDTO.getId());

        if(partyOpt.isEmpty()){ //no -> crear party
            party = this.prizeFeign.createParty(barTableDTO.getId());
        }else{
            party = partyOpt.get();
        }

        this.prizeFeign.asignClientToParty(party.getId(),clientDTO.getId());
    }
}
