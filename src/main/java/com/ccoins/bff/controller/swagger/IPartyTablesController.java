package com.ccoins.bff.controller.swagger;

import io.swagger.annotations.Api;

import static com.ccoins.bff.controller.swagger.SwaggerConstants.PARTY;
import static com.ccoins.bff.controller.swagger.SwaggerConstants.TABLE;

@Api(tags = PARTY + " " + TABLE)
public interface IPartyTablesController {


}
