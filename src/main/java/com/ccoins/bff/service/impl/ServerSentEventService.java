package com.ccoins.bff.service.impl;

import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.dto.users.ClientDTO;
import com.ccoins.bff.feign.BarsFeign;
import com.ccoins.bff.feign.UsersFeign;
import com.ccoins.bff.service.IServerSentEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static com.ccoins.bff.utils.SSEConstants.OWNER_CODE;

@Service
@Slf4j
public class ServerSentEventService implements IServerSentEventService {

    private final BarsFeign barsFeign;
    private final UsersFeign usersFeign;

    //Map<BarId,Map<ClientIp,Emitter>>
    protected static final Map<Long, Map<String,SseEmitter>> emitters = new ConcurrentHashMap<>();

    @Autowired
    public ServerSentEventService(BarsFeign barsFeign, UsersFeign usersFeign) {
        this.barsFeign = barsFeign;
        this.usersFeign = usersFeign;
    }

    @Override
    public SseEmitter subscribeOwner(Long barId){

        String client = "0";
        return this.subscribe(barId,client);
    }

    @Override
    public SseEmitter subscribeClient(Long partyId, String client){

        ResponseEntity<IdDTO> responseEntity = this.barsFeign.getBarIdByParty(partyId);

        if(!responseEntity.hasBody()){
            return  new SseEmitter(Long.MAX_VALUE);
        }

        Long barId = Objects.requireNonNull(responseEntity.getBody()).getId();

        return this.subscribe(barId, client);
    }


    @Override
    public SseEmitter subscribe(Long barId, String client){

        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        this.sendInitEvent(sseEmitter);

        Map<String,SseEmitter> sseEmitterMap = emitters.get(barId);

        if(sseEmitterMap == null || sseEmitterMap.isEmpty()){
            sseEmitterMap = new ConcurrentHashMap<>();
        }

        sseEmitterMap.put(client,sseEmitter);

        emitters.put(barId,sseEmitterMap);

        sseEmitter.onCompletion(()->emitters.get(barId).remove(client));
        sseEmitter.onTimeout(()->emitters.get(barId).remove(client));
        sseEmitter.onError(e->emitters.get(barId).remove(client));

        return sseEmitter;
    }

    @Override
    @Async
    public void dispatchEventToAllClientsFromBar(String eventName, Object data, Long barId){

        Map<String,SseEmitter> sseEmitterMap = emitters.get(barId);

        if(sseEmitterMap != null && !sseEmitterMap.isEmpty()){

            sseEmitterMap.forEach((client,emitter) -> {
                try{
                    if(!OWNER_CODE.equals(client))
                        emitter.send(this.buildEvent(eventName, data));
                }catch(IOException e){
                    sseEmitterMap.remove(client);
                    log.error("Error while sending event to all clients from bar.");
                }
            });
            emitters.put(barId, sseEmitterMap);
        }
    }

    @Override
    @Async
    public void dispatchEventToAllClientsFromBarAndBarToo(String eventName, Object data, Long barId){

        Map<String,SseEmitter> sseEmitterMap = emitters.get(barId);

        if(sseEmitterMap != null && !sseEmitterMap.isEmpty()){

            sseEmitterMap.forEach((client,emitter) -> {
                try{
                    emitter.send(this.buildEvent(eventName, data));
                }catch(IOException e){
                    sseEmitterMap.remove(client);
                    log.error("Error while sending event to all clients and bar.");
                }
            });
            emitters.put(barId, sseEmitterMap);
        }
    }

    @Override
    @Async
    public void dispatchEventToSomeClientsFromBar(String eventName, Object data, Long barId, List<String> clients){

        Map<String,SseEmitter> sseEmitterMap = emitters.get(barId);

        if(sseEmitterMap != null && !sseEmitterMap.isEmpty()) {

            clients.forEach(client -> {
                try{
                    if(sseEmitterMap.get(client) != null)
                        sseEmitterMap.get(client).send(this.buildEvent(eventName, data));
                }catch(IOException e){
                    sseEmitterMap.remove(client);
                    log.error("Error while sending event to some clients.");
                }
            });
            emitters.put(barId, sseEmitterMap);
        }
    }


    private byte[] parseDataToBytes(Object data){
        try {
            String jsonData;
            ObjectMapper objectMapper = new ObjectMapper();
            jsonData = objectMapper.writeValueAsString(data);
            return jsonData.getBytes(StandardCharsets.UTF_8);
        }catch(Exception e){
            log.error("Error trying to parse data to json");
            return new byte[0];
        }
    }

    @Override
    @Async
    public void dispatchEventToClientsFromParty(String eventName, Object data, Long partyId){

        ResponseEntity<IdDTO> barResponse = this.barsFeign.getBarIdByParty(partyId);

        if(!barResponse.hasBody())
            return;

        IdDTO barId = barResponse.getBody();

        if(barId == null)
            return;

        Map<String,SseEmitter> sseEmitterMap = emitters.get(barId.getId());
        ResponseEntity<List<ClientDTO>> clientsResponse = this.usersFeign.findActiveByParty(partyId);

        if(clientsResponse.hasBody() && sseEmitterMap != null && !sseEmitterMap.isEmpty()){

            List<ClientDTO> clients = clientsResponse.getBody();

            if(clients == null)
                return;

            clients.forEach(client -> {
                try{
                    SseEmitter emitter = sseEmitterMap.get(client.getIp());

                    if(emitter != null)
                        emitter.send(this.buildEvent(eventName, data));
                }catch(IOException e){
                    sseEmitterMap.remove(client.getIp());
                    log.error("Error while sending event to clients from party.");
                }
            });
            emitters.put(barId.getId(), sseEmitterMap);
        }
    }

    @Override
    public void dispatchEventToClientsFromParties(String eventName, Object data, List<Long> parties) {
        parties.parallelStream().forEach(party -> this.dispatchEventToClientsFromParty(eventName,data,party));
    }

    @Override
    public void dispatchEventToSingleBar(String eventName, Object data, Long barId){

        Map<String,SseEmitter> sseEmitterMap = emitters.get(barId);
        try{
            sseEmitterMap.get(OWNER_CODE).send(this.buildEvent(eventName, data));
        }catch(IOException e){
            sseEmitterMap.remove(OWNER_CODE);
            log.error("Error while sending event to owner.");
        }
    }

    @Override
    public void dispatchEventToAllBars(String eventName, Object data){

        emitters.forEach((barId, sseEmitterMap) -> {
            try{
                sseEmitterMap.get(OWNER_CODE).send(this.buildEvent(eventName, data));
            }catch(IOException e){
                sseEmitterMap.remove(OWNER_CODE);
                log.error("Error while sending event to owner.");
            }
        });
    }

    private void sendInitEvent(SseEmitter sseEmitter){
        try{
            sseEmitter.send(SseEmitter.event().name("INIT"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    private SseEmitter.SseEventBuilder buildEvent(String eventName,  Object data){

        byte[] encodedData = null;
        if (data != null) {
            encodedData = parseDataToBytes(data);
        }
        byte[] finalEncodedData = encodedData;

        return SseEmitter.event().name(eventName).data(finalEncodedData != null && finalEncodedData.length != 0? finalEncodedData : "", new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8));
    }
}
