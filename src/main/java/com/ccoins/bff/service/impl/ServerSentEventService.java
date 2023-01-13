package com.ccoins.bff.service.impl;

import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.feign.BarsFeign;
import com.ccoins.bff.service.IServerSentEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ServerSentEventService implements IServerSentEventService {

    @Autowired
    private BarsFeign barsFeign;



    protected static final Map<Long, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    @Override
    public SseEmitter subscribe(@RequestParam Long partyId){

        ResponseEntity<IdDTO> responseEntity = this.barsFeign.getBarIdByParty(partyId);
        Long barId;
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        if(responseEntity.hasBody()){
            barId = Objects.requireNonNull(responseEntity.getBody()).getId();

            this.sendInitEvent(sseEmitter);

            List<SseEmitter> sseEmitterList = emitters.get(barId);

            if(sseEmitterList == null || sseEmitterList.isEmpty()){
                sseEmitterList = new CopyOnWriteArrayList<>();
            }

            sseEmitterList.add(sseEmitter);

            emitters.put(barId,sseEmitterList);

            sseEmitter.onCompletion(()->emitters.get(barId).remove(sseEmitter));
            sseEmitter.onTimeout(()->emitters.get(barId).remove(sseEmitter));
            sseEmitter.onError(e->emitters.get(barId).remove(sseEmitter));
        }
        return sseEmitter;
    }

    @Override
    public void dispatchEventToClients(String eventName, Object data, Long barId){

        List<SseEmitter> sseEmitterList = emitters.get(barId);

        if(sseEmitterList != null && !sseEmitterList.isEmpty()){

            for (SseEmitter emitter: sseEmitterList) {
                try{
                    emitter.send(SseEmitter.event().name(eventName).data(data));
                }catch(IOException e){
                    sseEmitterList.remove(emitter);
                    e.printStackTrace();
                }
            }
            emitters.put(barId, sseEmitterList);
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
