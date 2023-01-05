package com.ccoins.bff.controller;

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
public class ServerSentEventController {

    @Autowired
    private IServerSentEventService service;

    // method for client subscription
    @CrossOrigin(origins = "${sse.cross-origins}")
    @RequestMapping(value="/subscribe",consumes=MediaType.ALL_VALUE)
    public SseEmitter subscribe(@RequestParam Long partyId){
        return this.service.subscribe(partyId);
    }

}