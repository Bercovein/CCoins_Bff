package com.ccoins.Bff.controller.swagger;

import com.ccoins.Bff.dto.GenericRsDTO;
import com.ccoins.Bff.dto.IdDTO;
import com.ccoins.Bff.dto.ListDTO;
import com.ccoins.Bff.dto.bars.TableDTO;
import com.ccoins.Bff.dto.bars.TableQuantityDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import static com.ccoins.Bff.controller.swagger.SwaggerConstants.*;

@Api(tags = TABLE)
public interface ITablesController {

    @ApiOperation(value = SAVE_OR_UPDATE)
    ResponseEntity<TableDTO> saveOrUpdate(@RequestBody TableDTO request);

    @ApiOperation(value = FIND_BY_ID)
    ResponseEntity<TableDTO> findById(@RequestBody IdDTO id);

    @ApiOperation(value = FIND_ALL_BY + BAR)
    ResponseEntity<ListDTO> findAllByBar(@RequestBody IdDTO request);

    @ApiOperation(value = ACTIVATE_DEACTIVATE)
    ResponseEntity<TableDTO> active(@RequestBody IdDTO id);

    @ApiOperation(value = SAVE_BY_QUANTITY)
    ResponseEntity<GenericRsDTO> createByQuantity(@RequestBody TableQuantityDTO request);

    @ApiOperation(value = DELETE_BY_QUANTITY)
    ResponseEntity<GenericRsDTO> deleteByQuantity(@RequestBody TableQuantityDTO request);
}
