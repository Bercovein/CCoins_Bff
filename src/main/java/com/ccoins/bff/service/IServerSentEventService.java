package com.ccoins.bff.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface IServerSentEventService {


    SseEmitter subscribe(Long partyId, String client);

    @Async
    void dispatchEventToAllClientsFromBar(String eventName, Object data, Long barId);

    @Async
    void dispatchEventToSomeClientsFromBar(String eventName, Object data, Long barId, List<String> clients);

    @Async
    void dispatchEventToClientsFromParty(String eventName, Object data, Long partyId);

    void dispatchEventToClientsFromParties(String eventName, Object data, List<Long> parties);
}
