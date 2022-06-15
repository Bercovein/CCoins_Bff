package com.ccoins.Bff.controller;

import com.ccoins.Bff.controller.swagger.IImageController;
import com.ccoins.Bff.dto.TableListQrRsDTO;
import com.ccoins.Bff.service.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/images")
@CrossOrigin
public class ImageController implements IImageController {

    private final IImageService service;

    @Autowired
    public ImageController(IImageService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void generatePDFWithQRCodes(@RequestBody TableListQrRsDTO tableList){
        this.service.generatePDFWithQRCodes(tableList);
    }

}
