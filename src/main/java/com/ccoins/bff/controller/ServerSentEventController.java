package com.ccoins.bff.controller;

import com.ccoins.bff.controller.swagger.IServerSentEventController;
import com.ccoins.bff.service.IServerSentEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse")
public class ServerSentEventController implements IServerSentEventController {

    private final IServerSentEventService service;

    @Autowired
    public ServerSentEventController(IServerSentEventService service) {
        this.service = service;
    }

    @CrossOrigin(origins = "${sse.cross-origins}")
    @RequestMapping(value="/subscribe",consumes=MediaType.ALL_VALUE)
    @Override
    public SseEmitter subscribeClient(@RequestParam("partyId") Long partyId, @RequestParam("client") String client){
        return this.service.subscribeClient(partyId, client);
    }

    @CrossOrigin(origins = "${sse.cross-origins}")
    @RequestMapping(value="/subscribe/owner",consumes=MediaType.ALL_VALUE)
    @Override
    public SseEmitter subscribeOwner(@RequestParam("barId") Long barId){
        return this.service.subscribeOwner(barId);
    }

}