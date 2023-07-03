package com.ccoins.bff.service.impl;

import com.ccoins.bff.BffApplication;
import com.ccoins.bff.dto.LongListDTO;
import com.ccoins.bff.dto.bars.BarTableDTO;
import com.ccoins.bff.dto.image.ImageToPdfDTO;
import com.ccoins.bff.dto.image.RowToPdfDTO;
import com.ccoins.bff.exceptions.BadRequestException;
import com.ccoins.bff.exceptions.constant.ExceptionConstant;
import com.ccoins.bff.feign.BarsFeign;
import com.ccoins.bff.service.IImageService;
import com.ccoins.bff.service.ITablesService;
import com.ccoins.bff.utils.DateUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;
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

    private final String url;

    private final String jasperReportQrPath;

    private final String tempFolderPath;

    private final String imagesFolderPath;

    private final String logoName;

    private static final String PNG = "png";

    private final ITablesService tablesService;

    @Autowired
    public ImageService(@Value("${api.url.path}") String url,
                        @Value("${jasper-report.qr.path}") String jasperReportQrPath,
                        @Value("${folder.temp.path}") String tempFolderPath,
                        @Value("${folder.images.path}") String imagesFolderPath,
                        @Value("${folder.images.logo.name}") String logoName,
                        BarsFeign barsFeign, ITablesService tablesService) {
        super(barsFeign);
        this.url = url;
        this.jasperReportQrPath = jasperReportQrPath;
        this.tempFolderPath = tempFolderPath;
        this.imagesFolderPath = imagesFolderPath;
        this.logoName = logoName;
        this.tablesService = tablesService;
    }

    @Override
    public  ResponseEntity<byte[]> generatePDFWithQRCodes(LongListDTO request) throws JRException, IOException {

        List<ImageToPdfDTO> imageList = new ArrayList<>();

        List<BarTableDTO> listed = this.tablesService.findByIdIn(request);

        //generar QRs
        for (BarTableDTO table : listed) {
            log.error("CREANDO PDF:");
            InputStream inputStream = this.createQRImage(this.url.concat("/login").concat(table.getCode()), table.getCode());
            imageList.add(ImageToPdfDTO.builder().number(table.getNumber()).image(inputStream).build());
        }

        ResponseEntity<byte[]> response = this.generatePdfFromList(imageList);

        this.deleteImagesByList(imageList);

        return response;
    }

    public  ResponseEntity<byte[]> generatePdfFromList(List<ImageToPdfDTO> list) throws JRException, IOException {
        try {
            log.error("GENERATING PDF: generatePdfFromList");
            JasperReport report = JasperCompileManager.compileReport(BffApplication.class.getResourceAsStream(jasperReportQrPath));

            List<RowToPdfDTO> rowList = this.imagesToRows(list);

            JRBeanCollectionDataSource jcd = new JRBeanCollectionDataSource(rowList);
            JasperPrint print = JasperFillManager.fillReport(report, null, jcd);

            String tempPath = tempFolderPath.concat(DateUtils.nowCurrentMillis()).concat(".pdf");

            OutputStream output = new FileOutputStream(tempPath);
            log.error("CREANDO JASPER REPORT");
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
        long fileLength;
        final byte[] bytes = Files.readAllBytes(file.toPath());
        fileLength = file.length();

        file.delete();

        return ResponseEntity.ok()
                .contentLength(fileLength)
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
            log.error("MatrixToImageConfig config = new MatrixToImageConfig(MatrixToImageConfig.BLACK, MatrixToImageConfig.WHITE);\n");

            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, config);
            log.error("BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, config);\n");

            File file = new File(imagesFolderPath.concat(logoName));
            BufferedImage logoImage = ImageIO.read(file);

            int deltaHeight = qrImage.getHeight() - logoImage.getHeight();
            int deltaWidth = qrImage.getWidth() - logoImage.getWidth();

            BufferedImage combined = new BufferedImage(qrImage.getHeight(), qrImage.getWidth(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) combined.getGraphics();
            g.drawImage(qrImage, 0, 0, null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            g.drawImage(logoImage, deltaWidth / 2, deltaHeight / 2, null);

            String filePath = tempFolderPath.concat(fileName.concat(".").concat(PNG));
            ImageIO.write(combined, PNG, new File(filePath));

            return Files.newInputStream(Path.of(filePath), new StandardOpenOption[]{StandardOpenOption.DELETE_ON_CLOSE});
        }catch(Exception e){
            throw new BadRequestException(ExceptionConstant.QR_CODE_GENERATION_ERROR_CODE,
                    this.getClass(), ExceptionConstant.QR_CODE_GENERATION_ERROR);
        }
    }

}
