package com.ccoins.bff.controller;

import com.ccoins.bff.dto.IdDTO;
import com.ccoins.bff.feign.BarsFeign;
import com.ccoins.bff.service.ISpotifyService;
import com.ccoins.bff.spotify.sto.PlaybackSPTF;
import com.ccoins.bff.utils.enums.EventNamesEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/sse")
public class SSEController {

    @Autowired
    private BarsFeign barsFeign;

    private ISpotifyService spotifyService;

    public Map<Long, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    // method for client subscription
    @CrossOrigin(origins = "${sse.cross-origins}")
    @RequestMapping(value="/subscribe",consumes=MediaType.ALL_VALUE)
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

    public void dispatchEventToClients(String eventName, Object data, Long barId){

        List<SseEmitter> sseEmitterList = this.emitters.get(barId);

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

    @Scheduled(fixedDelayString = "${sse.cron}")
    public void sendProgramedMessages(){

        this.emitters.forEach((barId,emitterList) -> {
            this.dispatchSpotifyEvents(barId);
        });
    }

    private void dispatchSpotifyEvents(Long barId){
        PlaybackSPTF playback = this.spotifyService.getPlaybackByBarId(barId);
        this.dispatchEventToClients(EventNamesEnum.ACTUAL_SONG_SPTF.name(),playback, barId);
    }


    private void sendInitEvent(SseEmitter sseEmitter){
        try{
            sseEmitter.send(SseEmitter.event().name("INIT"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}