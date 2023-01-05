package com.ccoins.bff.service;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface IServerSentEventService {


    SseEmitter subscribe(@RequestParam Long partyId);

    void dispatchEventToClients(String eventName, Object data, Long barId);
}
