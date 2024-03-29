package com.ccoins.bff.controller.swagger;

import com.ccoins.bff.dto.*;
import com.ccoins.bff.dto.bars.BarTableDTO;
import com.ccoins.bff.dto.bars.TableDTO;
import com.ccoins.bff.dto.bars.TableQuantityDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

import static com.ccoins.bff.controller.swagger.SwaggerConstants.*;

@Api(tags = TABLE)
public interface ITablesController {

    @ApiOperation(value = SAVE_OR_UPDATE)
    ResponseEntity<TableDTO> saveOrUpdate(@RequestBody TableDTO request);

    @ApiOperation(value = FIND_BY_ID)
    ResponseEntity<BarTableDTO> findById(@RequestBody IdDTO id);

    @ApiOperation(value = FIND_ALL_BY + BAR + AND_OR_STATUS)
    ResponseEntity<ListDTO> findAllByBar(@RequestBody IdDTO request, @PathVariable("status") Optional<String> status);

    @ApiOperation(value = ACTIVATE_DEACTIVATE)
    ResponseEntity<TableDTO> active(@RequestBody IdDTO id);

    @ApiOperation(value = SAVE_BY_QUANTITY)
    ResponseEntity<GenericRsDTO> createByQuantity(@RequestBody TableQuantityDTO request);

    @ApiOperation(value = DELETE_BY_QUANTITY)
    ResponseEntity<GenericRsDTO> deleteByQuantity(@RequestBody TableQuantityDTO request);

    @ApiOperation(value = "Change state of tables by id list")
    ResponseEntity<GenericRsDTO> activeByList(@RequestBody LongListDTO request);

    @ApiOperation(value = "Generate qr codes by table id list")
    ResponseEntity<ResponseDTO> generateCodesByList(@RequestBody LongListDTO request);
}
