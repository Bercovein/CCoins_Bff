package com.ccoins.bff.service.impl;

import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.feign.BarsFeign;
import com.ccoins.bff.service.IServerSentEventService;
import com.ccoins.bff.utils.HeaderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ServerSentEventService implements IServerSentEventService {

    private BarsFeign barsFeign;
    protected static final Map<Long, Map<String,SseEmitter>> emitters = new ConcurrentHashMap<>();

    @Autowired
    public ServerSentEventService(BarsFeign barsFeign) {
        this.barsFeign = barsFeign;
    }

    @Override
    public SseEmitter subscribe(Long partyId, String client){

        ResponseEntity<IdDTO> responseEntity = this.barsFeign.getBarIdByParty(partyId);
        Long barId;
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        if(responseEntity.hasBody()){
            barId = Objects.requireNonNull(responseEntity.getBody()).getId();

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
        }
        return sseEmitter;
    }

    @Override
    @Async
    public void dispatchEventToAllClientsFromBar(String eventName, Object data, Long barId){

        Map<String,SseEmitter> sseEmitterMap = emitters.get(barId);

        if(sseEmitterMap != null && !sseEmitterMap.isEmpty()){

            sseEmitterMap.forEach((client,emitter) -> {
                try{
                    emitter.send(SseEmitter.event().name(eventName).data(data, MediaType.APPLICATION_JSON));
                }catch(IOException e){
                    sseEmitterMap.remove(client);
                    e.printStackTrace();
                }
            });
            emitters.put(barId, sseEmitterMap);
        }
    }

    @Override
    @Async
    public void dispatchEventToSomeClientsFromBar(String eventName, Object data, Long barId, List<String> clients){

        Map<String,SseEmitter> sseEmitterMap = emitters.get(barId);

        if(sseEmitterMap != null && !sseEmitterMap.isEmpty()){

            clients.forEach(client -> {
                try{
                    sseEmitterMap.get(client).send(SseEmitter.event().name(eventName).data(data, MediaType.APPLICATION_JSON));
                }catch(IOException e){
                    sseEmitterMap.remove(client);
                    e.printStackTrace();
                }
            });
            emitters.put(barId, sseEmitterMap);
        }
    }

    private void sendInitEvent(SseEmitter sseEmitter){
        try{
            sseEmitter.send(SseEmitter.event().name("INIT"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
