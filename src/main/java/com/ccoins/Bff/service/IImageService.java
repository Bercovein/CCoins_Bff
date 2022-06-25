package com.ccoins.Bff.service;

import com.ccoins.Bff.dto.TableListQrRsDTO;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public interface IImageService {

    BufferedImage generateQr(String qrCodeText, int width, int height) throws Exception;

    InputStream createQRImage(String text, String fileName) throws Exception;

    ResponseEntity<byte[]> generatePDFWithQRCodes(TableListQrRsDTO tableList) throws JRException, IOException;
}
