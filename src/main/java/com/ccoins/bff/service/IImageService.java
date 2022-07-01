package com.ccoins.bff.service;

import com.ccoins.bff.dto.LongListDTO;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public interface IImageService {

    BufferedImage generateQr(String qrCodeText, int width, int height) throws Exception;

    InputStream createQRImage(String text, String fileName) throws Exception;

    ResponseEntity<byte[]> generatePDFWithQRCodes(LongListDTO tableList) throws JRException, IOException;
}
