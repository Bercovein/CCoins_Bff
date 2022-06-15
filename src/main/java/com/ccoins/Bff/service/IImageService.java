package com.ccoins.Bff.service;

import com.ccoins.Bff.dto.TableListQrRsDTO;

import java.awt.image.BufferedImage;

public interface IImageService {

    BufferedImage generateQr(String qrCodeText, int width, int height) throws Exception;

    void createQRImage(String text, String fileName) throws Exception;

    void generatePDFWithQRCodes(TableListQrRsDTO tableList);
}
