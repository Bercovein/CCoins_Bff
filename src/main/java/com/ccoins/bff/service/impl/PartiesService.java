package com.ccoins.bff.service.impl;

import com.ccoins.bff.dto.*;
import com.ccoins.bff.dto.bars.BarTableDTO;
import com.ccoins.bff.dto.prizes.ClientPartyDTO;
import com.ccoins.bff.dto.prizes.PartyDTO;
import com.ccoins.bff.dto.users.ClientDTO;
import com.ccoins.bff.exceptions.BadRequestException;
import com.ccoins.bff.exceptions.constant.ExceptionConstant;
import com.ccoins.bff.feign.BarsFeign;
import com.ccoins.bff.feign.PrizeFeign;
import com.ccoins.bff.feign.UsersFeign;
import com.ccoins.bff.service.*;
import com.ccoins.bff.utils.HeaderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.ccoins.bff.utils.enums.EventNamesEnum.NEW_LEADER;

@Service
public class PartiesService extends ContextService implements IPartiesService {

    private final PrizeFeign prizeFeign;

    private final ITablesService tablesService;

    private final UsersFeign usersFeign;

    private final ICoinsService coinsService;

    private final IServerSentEventService sseService;

    private final IRandomNameService randomizer;

    @Autowired
    public PartiesService(PrizeFeign prizeFeign, BarsFeign barsFeign, ITablesService tablesService, UsersFeign usersFeign, ICoinsService coinsService, IServerSentEventService sseService, IRandomNameService randomizer) {
        super(barsFeign);
        this.prizeFeign = prizeFeign;
        this.tablesService = tablesService;
        this.usersFeign = usersFeign;
        this.coinsService = coinsService;
        this.sseService = sseService;
        this.randomizer = randomizer;
    }


    @Override
    public Long asignOrCreatePartyByCode(String code, ClientDTO clientDTO) {

        PartyDTO party;
        Optional<PartyDTO> partyOpt;
        boolean leader = false;
        //hay party activa en la mesa?
            //traer mesa por codigo y party por id de mesa
        BarTableDTO barTableDTO = this.tablesService.findByCode(code);

        partyOpt = this.findActivePartyByTable(barTableDTO.getId());

        if(partyOpt.isEmpty()){ //no -> crear party
            party = this.createParty(barTableDTO.getId());
            leader = true;
        }else{
            party = partyOpt.get();
        }

        this.asignClientToParty(party.getId(),clientDTO.getId(), leader);

        return party.getId();
    }

    @Override
    public void asignClientToParty(Long partyId, Long clientId, boolean leader){

        try {
            this.prizeFeign.asignClientToParty(partyId, clientId, leader);
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
                    .active(true)
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
    public PartyDTO findActivePartyByTableCode(String code){

        Optional<PartyDTO> partyOpt = this.prizeFeign.findActivePartyByTableCode(code);

        if(partyOpt.isEmpty()){
            throw new BadRequestException(ExceptionConstant.PARTY_BY_ERROR_CODE,this.getClass(),ExceptionConstant.PARTY_BY_ERROR);
        }

        return partyOpt.get();
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
        List<ClientPartyDTO> list;
        List<ClientDTO> clients = new ArrayList<>();

        try {
            list = this.prizeFeign.findClientsByPartyId(id);
        }catch(Exception e){
            throw new BadRequestException(ExceptionConstant.PARTY_CLIENTS_ERROR_CODE,
                    this.getClass(), ExceptionConstant.PARTY_CLIENTS_ERROR);
        }

        List<Long> idList = new ArrayList<>();
        list.forEach(cp -> idList.add(cp.getClient()));

        if(!list.isEmpty()){
            clients = this.findByIdIn(idList);
        }

        clients.removeIf(clientDTO -> clientDTO.getIp().equals(clientIp));

        for (ClientDTO client: clients) {

            Optional<ClientPartyDTO> cpOpt = list.stream().filter( c -> c.getClient().equals(client.getId())).findAny();
            if(cpOpt.isPresent()){
                client.setLeader(cpOpt.get().isLeader());
            }else{
                client.setLeader(false);
            }
        }

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

    @Override
    public List<Long> findAllIdsByClients(List<Long> clientsIds) {

        try {
            return this.prizeFeign.findAllIdsByClients(LongListDTO.builder().list(clientsIds).build());
        } catch (Exception e) {
            throw new BadRequestException(ExceptionConstant.CLIENTS_LIST_ERROR_CODE,
                    this.getClass(), ExceptionConstant.CLIENTS_LIST_ERROR);
        }
    }

    @Override
    public ResponseEntity<GenericRsDTO<ResponseDTO>> giveLeaderTo(HttpHeaders headers, IdDTO request) {

        Long leader = Long.getLong(HeaderUtils.getClient(headers));
        Long newLeader = request.getId();

        Long partyId = HeaderUtils.getPartyId(headers);

        ResponseEntity<GenericRsDTO<ResponseDTO>> response = this.prizeFeign.giveLeaderTo(leader, newLeader);

        if(response.hasBody() && Objects.requireNonNull(response.getBody()).getCode() == null){
            this.sseService.dispatchEventToClientsFromParty(NEW_LEADER.name(),response.getBody().getMessage(),partyId);
        }

        return response;
    }
}
