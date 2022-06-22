package com.ccoins.Bff.controller.swagger;

import com.ccoins.Bff.dto.TableListQrRsDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.jasperreports.engine.JRException;
import org.springframework.web.bind.annotation.RequestBody;

import static com.ccoins.Bff.controller.swagger.SwaggerConstants.*;

@Api(tags = TABLE)
public interface IImageController {

    @ApiOperation(value = GENERATE_QR_PDF)
    void generatePDFWithQRCodes(@RequestBody TableListQrRsDTO tableList) throws JRException;
}