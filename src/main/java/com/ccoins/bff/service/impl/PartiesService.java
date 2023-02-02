package com.ccoins.bff.service.impl;

import com.ccoins.bff.dto.ListDTO;
import com.ccoins.bff.dto.LongDTO;
import com.ccoins.bff.dto.bars.BarTableDTO;
import com.ccoins.bff.dto.prizes.PartyDTO;
import com.ccoins.bff.dto.users.ClientDTO;
import com.ccoins.bff.exceptions.BadRequestException;
import com.ccoins.bff.exceptions.constant.ExceptionConstant;
import com.ccoins.bff.feign.PrizeFeign;
import com.ccoins.bff.feign.UsersFeign;
import com.ccoins.bff.service.ICoinsService;
import com.ccoins.bff.service.IPartiesService;
import com.ccoins.bff.service.IRandomNameService;
import com.ccoins.bff.service.ITablesService;
import com.ccoins.bff.utils.HeaderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PartiesService extends ContextService implements IPartiesService {

    private final PrizeFeign prizeFeign;

    private final ITablesService tablesService;

    private final UsersFeign usersFeign;

    private final ICoinsService coinsService;

    private final IRandomNameService randomizer;

    @Autowired
    public PartiesService(PrizeFeign prizeFeign, ITablesService tablesService, UsersFeign usersFeign, ICoinsService coinsService, IRandomNameService randomizer) {
        this.prizeFeign = prizeFeign;
        this.tablesService = tablesService;
        this.usersFeign = usersFeign;
        this.coinsService = coinsService;
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

    @Override
    public Optional<PartyDTO> findActivePartyByTableCode(String code){

        try {
            return this.prizeFeign.findActivePartyByTableCode(code);
        }catch (Exception e){
            throw new BadRequestException(ExceptionConstant.PARTY_BY_ERROR_CODE,
                    this.getClass(), ExceptionConstant.PARTY_BY_ERROR);
        }
    }

    @Override
    public LongDTO countCoinsByParty(Long id){
        Long quantity = this.coinsService.countCoinsByParty(id);
        return LongDTO.builder().value(quantity).build();
    }

    @Override
    public PartyDTO findById(Long id) {
        try {
            return this.prizeFeign.findById(id).get();
        }catch(Exception e){
            throw new BadRequestException(ExceptionConstant.PARTY_ID_ERROR_CODE,
                    this.getClass(), ExceptionConstant.PARTY_ID_ERROR);
        }
    }

    @Override
    public ListDTO findClientsFromParty(Long id, HttpHeaders headers) {

        String clientIp = HeaderUtils.getClient(headers);
        List<Long> longList;
        List<ClientDTO> clients = new ArrayList<>();

        try {
            longList = this.prizeFeign.findClientsByPartyId(id);
        }catch(Exception e){
            throw new BadRequestException(ExceptionConstant.PARTY_CLIENTS_ERROR_CODE,
                    this.getClass(), ExceptionConstant.PARTY_CLIENTS_ERROR);
        }

        if(!longList.isEmpty()){
            clients = this.findByIdIn(longList);
        }

        clients.removeIf(clientDTO -> clientDTO.getIp().equals(clientIp));

        clients = clients.stream().sorted(Comparator.comparing(ClientDTO::getNickName)).collect(Collectors.toList());

        return ListDTO.builder().list(clients).build();
    }

    @Override
    public void logout(String client) {
        try {
            this.prizeFeign.logoutClientFromTables(client);
        } catch (Exception e) {
            throw new BadRequestException(ExceptionConstant.LOGOUT_CLIENT_ERROR_CODE,
                    this.getClass(), ExceptionConstant.LOGOUT_CLIENT_ERROR);
        }
    }

    @Override
    public List<ClientDTO> findByIdIn(List<Long> list) {
        try {
            return this.usersFeign.findByIdIn(list);
        } catch (Exception e) {
            throw new BadRequestException(ExceptionConstant.CLIENTS_LIST_ERROR_CODE,
                    this.getClass(), ExceptionConstant.CLIENTS_LIST_ERROR);
        }
    }
}
