package com.ccoins.Bff.controller;

import com.ccoins.Bff.controller.swagger.IImageController;
import com.ccoins.Bff.dto.ListDTO;
import com.ccoins.Bff.service.IImageService;
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
    public  ResponseEntity<byte[]> generatePDFWithQRCodes(@RequestBody ListDTO tableList) throws JRException, IOException {
        return this.service.generatePDFWithQRCodes(tableList);
    }
}
