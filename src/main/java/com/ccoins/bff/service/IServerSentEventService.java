package com.ccoins.bff.service;

import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface IServerSentEventService {


    SseEmitter subscribe(@RequestParam HttpHeaders partyId);

    @Async
    void dispatchEventToAllClientsFromBar(String eventName, Object data, Long barId);

    @Async
    void dispatchEventToSomeClientsFromBar(String eventName, Object data, Long barId, List<String> clients);
}
