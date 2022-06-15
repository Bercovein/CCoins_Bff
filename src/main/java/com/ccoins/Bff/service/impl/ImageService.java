package com.ccoins.Bff.service.impl;

import com.ccoins.Bff.dto.TableListQrRsDTO;
import com.ccoins.Bff.dto.TableQrRsDTO;
import com.ccoins.Bff.exceptions.BadRequestException;
import com.ccoins.Bff.exceptions.constant.ExceptionConstant;
import com.ccoins.Bff.service.IImageService;
import lombok.extern.slf4j.Slf4j;
import net.glxn.qrgen.javase.QRCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

@Service
@Slf4j
public class ImageService extends ContextService implements IImageService {

    @Value("api.url.path")
    private String URL;

    private final static String JPG = "jpg";

    @Override
    public BufferedImage generateQr(final String qrCodeText, final int width, final int height) throws Exception {
        final ByteArrayOutputStream stream = QRCode
                .from(qrCodeText)
                .withSize(width, height)
                .stream();

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(stream.toByteArray());

        return ImageIO.read(byteArrayInputStream);
    }

    @Override
    public void createQRImage(String text, String fileName) {

        try {
            BufferedImage bufferedImage = this.generateQr(text, 350, 350);
            File codigoQR= new File(fileName.concat(".").concat(JPG));
            ImageIO.write(bufferedImage, JPG, codigoQR);
        }catch(Exception e){
            throw new BadRequestException(ExceptionConstant.QR_CODE_GENERATION_ERROR_CODE,
                    this.getClass(), ExceptionConstant.QR_CODE_GENERATION_ERROR);
        }
    }

    @Override
    public void generatePDFWithQRCodes(TableListQrRsDTO tableList) {

        List<TableQrRsDTO> list = tableList.getList();

        //generar QRs
        list.forEach(table -> {
            this.createQRImage(this.URL.concat("/").concat(table.getCode()), table.getCode());
        });

        //generar PDF

        //eliminar QRs

        //devolver PDF
    }
}
