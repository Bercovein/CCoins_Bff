package com.ccoins.bff.service.impl;

import com.ccoins.bff.dto.*;
import com.ccoins.bff.dto.bars.BarTableDTO;
import com.ccoins.bff.dto.prizes.ClientPartyDTO;
import com.ccoins.bff.dto.prizes.PartyDTO;
import com.ccoins.bff.dto.users.ClientDTO;
import com.ccoins.bff.exceptions.BadRequestException;
import com.ccoins.bff.exceptions.ObjectNotFoundException;
import com.ccoins.bff.exceptions.UnauthorizedException;
import com.ccoins.bff.exceptions.constant.ExceptionConstant;
import com.ccoins.bff.feign.BarsFeign;
import com.ccoins.bff.feign.PrizeFeign;
import com.ccoins.bff.feign.UsersFeign;
import com.ccoins.bff.service.*;
import com.ccoins.bff.utils.HeaderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.ccoins.bff.exceptions.constant.ExceptionConstant.*;
import static com.ccoins.bff.utils.enums.ClosePartyEnum.*;
import static com.ccoins.bff.utils.enums.EventNamesEnum.*;

@Service
@Slf4j
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
    public void asignOrCreatePartyByCode(ClientTableDTO request, ClientDTO clientDTO) {

        PartyDTO party;
        Optional<PartyDTO> partyOpt;
        boolean leader = false;
        //hay party activa en la mesa?
            //traer mesa por codigo y party por id de mesa
        BarTableDTO barTableDTO = this.tablesService.findByCode(request.getTableCode());

        partyOpt = this.findActivePartyByTable(barTableDTO.getId());

        if(partyOpt.isEmpty()){ //no -> crear party
            party = this.createParty(barTableDTO.getId());
            leader = true;
        }else{
            party = partyOpt.get();
        }

        this.asignClientToParty(party.getId(),clientDTO.getId(), leader);

        request.setPartyId(party.getId());
        request.setLeader(leader);
    }

    @Override
    public void asignClientToParty(Long partyId, Long clientId, boolean leader){

        try {
            ClientPartyDTO request = ClientPartyDTO.builder()
                    .client(clientId)
                    .party(partyId)
                    .active(true)
                    .leader(leader)
                    .build();
            this.prizeFeign.asignClientToParty(request);
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

        for (ClientDTO client: clients) {

            Optional<ClientPartyDTO> cpOpt = list.stream().filter( c -> c.getClient().equals(client.getId())).findAny();
            if(cpOpt.isPresent()){
                client.setLeader(cpOpt.get().isLeader());
            }else{
                client.setLeader(false);
            }
        }

        clients = clients.stream().sorted(Comparator.comparing(ClientDTO::getNickName)).collect(Collectors.toList());

        //pone al cliente actual en el inicio de la lista
        ClientDTO actualClient = clients.stream().filter(clientDTO -> clientDTO.getIp().equals(clientIp)).collect(Collectors.toList()).get(0);
        clients.removeIf(clientDTO -> clientDTO.getIp().equals(clientIp));
        clients.add(0, actualClient);

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

        String leader = HeaderUtils.getClient(headers);
        Long newLeader = request.getId();
        ResponseEntity<GenericRsDTO<ResponseDTO>> response;
        Long partyId = HeaderUtils.getPartyId(headers);

        try {
            response = this.prizeFeign.giveLeaderTo(leader, newLeader);
        }catch(Exception e){
            return ResponseEntity.ok(new GenericRsDTO<>(GIVE_LEADER_TO_ERROR_CODE,GIVE_LEADER_TO_ERROR,null));
        }

        if(response.hasBody() && Objects.requireNonNull(response.getBody()).getCode() == null){
            this.sseService.dispatchEventToClientsFromParty(NEW_LEADER.name(),response.getBody().getMessage(),partyId);

            ResponseEntity<IdDTO> barIdResponse = this.barsFeign.getBarIdByParty(partyId);

            if(barIdResponse.hasBody() && barIdResponse.getBody() != null){

                IdDTO idDTO = barIdResponse.getBody();
                String newLeaderIp;
                List<Long> newLeaderList = new ArrayList<>();
                newLeaderList.add(newLeader);

                List<ClientDTO> clientList = this.usersFeign.findByIdIn(newLeaderList);

                if(!clientList.isEmpty()) {
                    newLeaderIp = clientList.get(0).getIp();
                    this.sseService.dispatchEventToSomeClientsFromBar(YOU_ARE_THE_LEADER.name(), YOU_ARE_THE_LEADER.getMessage(),idDTO.getId(),List.of(newLeaderIp));
                }
            }
        }

        return response;
    }

    @Override
    public ResponseEntity<GenericRsDTO<ResponseDTO>> kickFromPartyByLeader(LongListDTO request, HttpHeaders headers) {

        String leaderIp = HeaderUtils.getClient(headers);
        Long partyId = HeaderUtils.getPartyId(headers);

        ResponseEntity<Boolean> isLeader = this.prizeFeign.isLeaderFromParty(leaderIp,partyId);

        if(!isLeader.hasBody() || isLeader.getBody() == null || !isLeader.getBody()){
            throw new ObjectNotFoundException(NO_LEADER_ERROR_CODE,this.getClass(),NO_LEADER_ERROR);
        }

        return this.kickFromParty(request.getList(), partyId, false);

    }

    @Override
    public ResponseEntity<GenericRsDTO<ResponseDTO>> kickFromPartyByOwner(LogoutPartyDTO request) {

        ResponseEntity<IdDTO> barIdOpt = this.barsFeign.getBarIdByParty(request.getPartyId());
        Long barId = super.findBarIdByOwner();

        if (!barIdOpt.hasBody() || (!Objects.equals(barId, Objects.requireNonNull(barIdOpt.getBody()).getId()))){
            throw new UnauthorizedException(NO_OWNER_FROM_PARTY_ERROR_CODE,this.getClass(),NO_OWNER_FROM_PARTY_ERROR);
        }

        return this.kickFromParty(request.getList(), request.getPartyId(), request.isBanned());

    }

    @Override
    public ResponseEntity<GenericRsDTO<ResponseDTO>> kickFromParty(List<Long> list, Long partyId, boolean banned) {

        List<ClientDTO> clientList;
        ResponseEntity<IdDTO> barId;
        try {
            clientList = this.usersFeign.findByIdIn(list);

            clientList.forEach(client -> {
                try {
                    this.logout(client.getIp());
                    if (banned) {
                        this.prizeFeign.banClientFromParty(client.getId(), partyId);
                    }
                } catch (Exception ignored) {
                    log.error("No se pudo kickear al cliente.");
                }
            });

            barId = this.barsFeign.getBarIdByParty(partyId);
        }catch(Exception e){
            return ResponseEntity.ok(new GenericRsDTO<>(CLOSED_PARTY_ERROR.getCode(),CLOSED_PARTY_ERROR.getMessage(),null));
        }

        List<String> clients = new ArrayList<>();
        list.forEach(c -> clients.add(c.toString()));

        //notifica al front para que le actualice la sesión
        if(barId.getBody() != null) {
            this.sseService.dispatchEventToSomeClientsFromBar(LOGOUT_CLIENT.name(), LOGOUT_CLIENT.getMessage(), barId.getBody().getId(), clients);
        }

        boolean response = false;

        try {
            response = this.prizeFeign.closePartyIfHaveNoClients(partyId);
        }catch(Exception e){
            log.error("No se pudo verificar el estado de la mesa.");
        }

        if(response){ //avisa si se cerró la party o solo se desloguearon los clientes
            return ResponseEntity.ok(new GenericRsDTO<>(CLOSED_PARTY.getCode(),CLOSED_PARTY.getMessage(),null));
        }

        return ResponseEntity.ok(new GenericRsDTO<>(CLIENTS_ALREADY_ON_PARTY.getCode(),CLIENTS_ALREADY_ON_PARTY.getMessage(),null));

    }

    @Override
    public boolean isBannedFromParty(ClientTableDTO request) {

        ResponseEntity<Boolean> response = this.prizeFeign.isBannedFromParty(request);

        if(!response.hasBody() || response.getBody() == null){
            return false;
        }

        return response.getBody();
    }

    @Override
    public ResponseEntity<List<PartyTableDTO>> findActivePartiesByOwner(){

        Long barId = super.findBarIdByOwner();
        List<PartyDTO> parties = new ArrayList<>();
        List<PartyTableDTO> response = new ArrayList<>();
        List<Long> tablesIds = new ArrayList<>();
        ResponseEntity<List<PartyDTO>> partiesResponse = this.prizeFeign.findActivePartiesByBar(barId);

        if(partiesResponse.hasBody()){
            parties = partiesResponse.getBody();
        }

        if(parties == null || parties.isEmpty()){
            return ResponseEntity.ok(response);
        }

        parties.forEach(party -> tablesIds.add(party.getTable()));

        List<BarTableDTO> tables = this.tablesService.findByIdIn(LongListDTO.builder().list(tablesIds).build());

        parties.forEach(partyTable -> {
            Long number = tables.stream().filter(table -> table.getId().equals(partyTable.getTable())).findFirst().orElseThrow().getNumber();
            response.add(PartyTableDTO.builder()
                            .id(partyTable.getId())
                            .active(partyTable.isActive())
                            .name(partyTable.getName())
                            .startDate(partyTable.getStartDate())
                            .table(partyTable.getTable())
                            .tableNumber(number)
                    .build());
        });

        return ResponseEntity.ok(response);
    }
}
