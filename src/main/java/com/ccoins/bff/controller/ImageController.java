package com.ccoins.bff.controller;

import com.ccoins.bff.controller.swagger.IImageController;
import com.ccoins.bff.dto.LongListDTO;
import com.ccoins.bff.service.IImageService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/images")
@CrossOrigin
public class ImageController implements IImageController {

    private final IImageService service;

    @Autowired
    public ImageController(IImageService service) {
        this.service = service;
    }

//    @RequestMapping(value = "", method = RequestMethod.POST, consumes="application/x-download")
//    @ResponseStatus(HttpStatus.CREATED)
//    public void generatePDFWithQRCodes(@RequestBody TableListQrRsDTO tableList) throws JRException {
//        this.service.generatePDFWithQRCodes(tableList);
//    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public  ResponseEntity<byte[]> generatePDFWithQRCodes(@RequestBody LongListDTO tableList) throws JRException, IOException {
        return this.service.generatePDFWithQRCodes(tableList);
    }
}
