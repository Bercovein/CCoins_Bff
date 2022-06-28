package com.ccoins.Bff.service.impl;

import com.ccoins.Bff.BffApplication;
import com.ccoins.Bff.dto.LongListDTO;
import com.ccoins.Bff.dto.bars.BarTableDTO;
import com.ccoins.Bff.dto.image.ImageToPdfDTO;
import com.ccoins.Bff.dto.image.RowToPdfDTO;
import com.ccoins.Bff.exceptions.BadRequestException;
import com.ccoins.Bff.exceptions.constant.ExceptionConstant;
import com.ccoins.Bff.service.IImageService;
import com.ccoins.Bff.service.ITablesService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;
import net.glxn.qrgen.javase.QRCode;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.*;

@Service
@Slf4j
public class ImageService extends ContextService implements IImageService {

    @Value("${api.url.path}")
    private String URL;

    @Value("${jasper-report.qr.path}")
    private String JASPER_REPORT_QR_PATH;

    @Value("${folder.temp.path}")
    private String TEMP_FOLDER_PATH;

    @Value("${folder.images.path}")
    private String IMAGES_FOLDER_PATH;

    @Value("${folder.images.logo.name}")
    private String LOGO_NAME;

    private static final String JPG = "jpg";
    private static final String PNG = "png";

    private final ITablesService tablesService;

    @Autowired
    public ImageService(ITablesService tablesService) {
        this.tablesService = tablesService;
    }

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
    public  ResponseEntity<byte[]> generatePDFWithQRCodes(LongListDTO request) throws JRException, IOException {

        List<ImageToPdfDTO> imageList = new ArrayList<>();

        List<BarTableDTO> listed = this.tablesService.findByIdIn(request);

        //generar QRs
        for (BarTableDTO table : listed) {
            InputStream inputStream = this.createQRImage(this.URL.concat("/").concat(table.getCode()), table.getCode());
            imageList.add(ImageToPdfDTO.builder().number(table.getNumber()).image(inputStream).build());
        }

        ResponseEntity<byte[]> response = this.generatePdfFromList(imageList);

        this.deleteImagesByList(imageList);

        return response;
    }

    public  ResponseEntity<byte[]> generatePdfFromList(List<ImageToPdfDTO> list) throws JRException, IOException {
        try {
            JasperReport report = JasperCompileManager.compileReport(BffApplication.class.getResourceAsStream(JASPER_REPORT_QR_PATH));

            List<RowToPdfDTO> rowList = this.imagesToRows(list);

            JRBeanCollectionDataSource jcd = new JRBeanCollectionDataSource(rowList);
            JasperPrint print = JasperFillManager.fillReport(report, null, jcd);

            String tempPath = TEMP_FOLDER_PATH.concat("JR.pdf");

            OutputStream output = new FileOutputStream(tempPath);
            JasperExportManager.exportReportToPdfStream(print, output);
            output.close();

            return this.generateResponseFile(tempPath);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private ResponseEntity<byte[]> generateResponseFile(String path) throws IOException {

        File file = new File(path);

        byte[] bytes = Files.readAllBytes(file.toPath());

        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);
    }

    private List<RowToPdfDTO> imagesToRows(List<ImageToPdfDTO> list) {

        List<RowToPdfDTO> rowList = new ArrayList<>();

        Iterator<ImageToPdfDTO> iterator = list.iterator();

        while(iterator.hasNext()){
            RowToPdfDTO row = new RowToPdfDTO();

            ImageToPdfDTO imageLeft = iterator.next();
            row.setNumberLeft(imageLeft.getNumber());
            row.setImageLeft(imageLeft.getImage());

            if(iterator.hasNext()){
                ImageToPdfDTO imageMedium = iterator.next();
                row.setNumberMedium(imageMedium.getNumber());
                row.setImageMedium(imageMedium.getImage());

                if(iterator.hasNext()){
                    ImageToPdfDTO imageRight = iterator.next();
                    row.setNumberRight(imageRight.getNumber());
                    row.setImageRight(imageRight.getImage());
                }
            }
            rowList.add(row);
        }
        return rowList;
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

    @Override
    public InputStream createQRImage(String text, String fileName) {

        Map hints = new HashMap();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = null;

        try {
            bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 250, 250, hints);
            MatrixToImageConfig config = new MatrixToImageConfig(MatrixToImageConfig.BLACK, MatrixToImageConfig.WHITE);

            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, config);

            File file = new File(IMAGES_FOLDER_PATH.concat(LOGO_NAME));
            BufferedImage logoImage = ImageIO.read(file);

            int deltaHeight = qrImage.getHeight() - logoImage.getHeight();
            int deltaWidth = qrImage.getWidth() - logoImage.getWidth();

            BufferedImage combined = new BufferedImage(qrImage.getHeight(), qrImage.getWidth(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) combined.getGraphics();
            g.drawImage(qrImage, 0, 0, null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            g.drawImage(logoImage, (int) Math.round(deltaWidth / 2), (int) Math.round(deltaHeight / 2), null);

            String filePath = TEMP_FOLDER_PATH.concat(fileName.concat(".").concat(PNG));
            ImageIO.write(combined, PNG, new File(filePath));

            return Files.newInputStream(Path.of(filePath), new StandardOpenOption[]{StandardOpenOption.DELETE_ON_CLOSE});
        }catch(Exception e){
            throw new BadRequestException(ExceptionConstant.QR_CODE_GENERATION_ERROR_CODE,
                    this.getClass(), ExceptionConstant.QR_CODE_GENERATION_ERROR);
        }
    }

}
