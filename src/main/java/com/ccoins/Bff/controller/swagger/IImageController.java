package com.ccoins.Bff.controller.swagger;

import com.ccoins.Bff.dto.TableListQrRsDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

import static com.ccoins.Bff.controller.swagger.SwaggerConstants.GENERATE_QR_PDF;
import static com.ccoins.Bff.controller.swagger.SwaggerConstants.TABLE;

@Api(tags = TABLE)
public interface IImageController {

    @ApiOperation(value = GENERATE_QR_PDF)
    ResponseEntity<byte[]> generatePDFWithQRCodes(@RequestBody TableListQrRsDTO tableList) throws JRException, IOException;
}
