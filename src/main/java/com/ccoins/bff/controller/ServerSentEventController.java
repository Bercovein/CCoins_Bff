package com.ccoins.bff.controller;

import com.ccoins.bff.controller.swagger.IServerSentEventController;
import com.ccoins.bff.service.IServerSentEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse")
public class ServerSentEventController implements IServerSentEventController {

    @Autowired
    private IServerSentEventService service;

    // method for client subscription
    @CrossOrigin(origins = "${sse.cross-origins}")
    @RequestMapping(value="/subscribe",consumes=MediaType.ALL_VALUE)
    @Override
    public SseEmitter subscribe(@RequestHeader HttpHeaders headers){
        return this.service.subscribe(headers);
    }

}