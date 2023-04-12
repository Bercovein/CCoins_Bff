package com.ccoins.bff.controller.swagger;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static com.ccoins.bff.controller.swagger.SwaggerConstants.SUBSCRIBE;

@Api(tags = "SERVER SENT EVENT")
public interface IServerSentEventController {

    @ApiOperation(value = SUBSCRIBE)
    SseEmitter subscribeClient(@RequestParam("partyId") Long partyId, @RequestParam("client") String client);

    // method for client subscription
    @CrossOrigin(origins = "${sse.cross-origins}")
    @RequestMapping(value="/subscribe/owner",consumes= MediaType.ALL_VALUE)
    SseEmitter subscribeOwner(@RequestParam("barId") Long barId);
}
