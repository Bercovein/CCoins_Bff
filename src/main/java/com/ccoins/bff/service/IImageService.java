package com.ccoins.bff.service;

import com.ccoins.bff.dto.LongListDTO;
import com.ccoins.bff.exceptions.BadRequestException;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;

public interface IImageService {

    InputStream createQRImage(String text, String fileName) throws BadRequestException;

    ResponseEntity<byte[]> generatePDFWithQRCodes(LongListDTO tableList) throws JRException, IOException;
}
