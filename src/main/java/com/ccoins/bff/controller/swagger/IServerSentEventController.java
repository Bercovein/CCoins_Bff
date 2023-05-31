package com.ccoins.bff.controller.swagger;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static com.ccoins.bff.controller.swagger.SwaggerConstants.SUBSCRIBE;

@Api(tags = "SERVER SENT EVENT")
public interface IServerSentEventController {

    @ApiOperation(value = SUBSCRIBE)
    SseEmitter subscribeClient(@RequestParam("partyId") Long partyId, @RequestParam("client") String client);

    @ApiOperation("Method to client subscription")
    SseEmitter subscribeOwner(@RequestParam("barId") Long barId);
}
