package com.ccoins.Bff.service.impl;

import com.ccoins.Bff.BffApplication;
import com.ccoins.Bff.dto.TableListQrRsDTO;
import com.ccoins.Bff.dto.TableQrRsDTO;
import com.ccoins.Bff.dto.image.ImageToPdfDTO;
import com.ccoins.Bff.exceptions.BadRequestException;
import com.ccoins.Bff.exceptions.constant.ExceptionConstant;
import com.ccoins.Bff.service.IImageService;
import lombok.extern.slf4j.Slf4j;
import net.glxn.qrgen.javase.QRCode;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ImageService extends ContextService implements IImageService {

    @Value("${api.url.path}")
    private String URL;

    @Value("${jasper-report.qr.path}")
    private String JASPER_REPORT_QR_PATH;

    @Value("${folder.images.path}")
    private String IMAGE_FOLDER_PATH;

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
    public InputStream createQRImage(String text, String fileName) {

        try {
            BufferedImage bufferedImage = this.generateQr(text, 1000, 1000);
            String filePath = IMAGE_FOLDER_PATH.concat(fileName.concat(".").concat(JPG));
            File qrCodeFile = new File(filePath);
            ImageIO.write(bufferedImage, JPG, qrCodeFile);
            return Files.newInputStream(Path.of(filePath), new StandardOpenOption[]{StandardOpenOption.DELETE_ON_CLOSE});
        }catch(Exception e){
            throw new BadRequestException(ExceptionConstant.QR_CODE_GENERATION_ERROR_CODE,
                    this.getClass(), ExceptionConstant.QR_CODE_GENERATION_ERROR);
        }
    }

    @Override
    public void generatePDFWithQRCodes(TableListQrRsDTO tableList)  {

        List<TableQrRsDTO> list = tableList.getList();
        List<ImageToPdfDTO> imageList = new ArrayList<>();

        //generar QRs
        for (TableQrRsDTO table : list) {
            InputStream inputStream = this.createQRImage(this.URL.concat("/").concat(table.getCode()), table.getCode());
            imageList.add(ImageToPdfDTO.builder().number(table.getNumber()).image(inputStream).build());
        }

        this.generatePdfFromList(imageList);

        this.deleteImagesByList(imageList);
    }

    public void generatePdfFromList(List<ImageToPdfDTO> list) {
        try {
            JasperReport report = JasperCompileManager.compileReport(BffApplication.class.getResourceAsStream(JASPER_REPORT_QR_PATH));
            JRBeanCollectionDataSource jcd = new JRBeanCollectionDataSource(list);
            JasperPrint print = JasperFillManager.fillReport(report, null, jcd);
            JasperViewer.viewReport(print, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteImagesByList(List<ImageToPdfDTO> imageList){

        imageList.forEach(img -> {
            try {
                img.getImage().close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
