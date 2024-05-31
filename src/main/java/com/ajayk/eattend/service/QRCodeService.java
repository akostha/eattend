package com.ajayk.eattend.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.ajayk.eattend.dto.QRCodeRequest;
import com.ajayk.eattend.dto.StatusObject;
import com.ajayk.eattend.model.Contact;
import com.ajayk.eattend.model.ContactRepository;
import com.ajayk.eattend.model.Event;
import com.ajayk.eattend.model.EventRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class QRCodeService {
	
	@Autowired
	EventRepository eventRepository;
	
	@Autowired
	ContactRepository contactRepository;
	
	@Autowired
	ImagePathService pathService;
	
	@Value("${service.qrservice.url}")
	String qrserviceUrl;
	
	private int imageWidth = 400;
	private int imageHeight = 300;
	
	public Mono<StatusObject> callQRCodeService(QRCodeRequest qrcodeRequest) {
		log.info("Method {} is called", "QRCodeService.createQrCode");
		Mono<StatusObject> result =  getQrServiceClient().post()
                .uri("/api/qrservice/qrcode") // Set the request URI
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(qrcodeRequest), QRCodeRequest.class) // Set the request body
                .retrieve() 
                .bodyToMono(StatusObject.class); // Retrieve the response body
		result.subscribe();
		return result;
    }
	
	
	public void createQRCode(QRCodeRequest qrcodeRequest) throws WriterException, IOException {
		
		Optional<Event> obj = eventRepository.findById(Long.valueOf(qrcodeRequest.getEvent().getId()));
		List<Contact> contacts =  contactRepository.findByEventId(qrcodeRequest.getEvent().getId());
		
		if(obj.isPresent() && !contacts.isEmpty()) {
			for (Contact element : contacts) {
				Event event  = obj.get();
				String msg = event.getDescr() + " at " + event.getEventDate();
				String[] txtMsgs = {"Hi "+ element.getFirstName(), "you are invited for",event.getDescr(),"on "+event.getEventDate()};
				String imgPath = pathService.getImagePath(event, element);
				createQRImage(msg, txtMsgs,imgPath, imageWidth, imageHeight);
				System.out.println("Image created at :" + imgPath);
	        }
		}
	}
	
	public void createTestQRCode(QRCodeRequest qrcodeRequest) throws WriterException, IOException {
		
		Optional<Event> obj = eventRepository.findById(Long.valueOf(qrcodeRequest.getEvent().getId()));
		List<Contact> contacts =  contactRepository.findByEventId(qrcodeRequest.getEvent().getId());
		String basePath = "C:\\devl\\copeland";
		String data = obj.isPresent() ? obj.get().toString() : "Invalid Event";
		BufferedImage bg1 = ImageIO.read(new File(basePath+"\\bg1.jpg")); //525x350
		
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 200, 200);
        BufferedImage qrcodeImg = MatrixToImageWriter.toBufferedImage(bitMatrix);
		
        BufferedImage resultImages = overlayImages(bg1, qrcodeImg, 50, 50);
        ImageIO.write(resultImages, "PNG", new File(basePath+"\\final.jpg"));
		
	}
	
	private BufferedImage overlayImages(BufferedImage baseImage, BufferedImage topImage, int dx, int dy) {
        // Create a new BufferedImage with transparency
        BufferedImage resultImage = new BufferedImage(
            baseImage.getWidth(), baseImage.getHeight(), BufferedImage.TYPE_INT_ARGB
        );

        //calculate 
        int[] pos = getQRCodeCord(baseImage.getWidth(), baseImage.getHeight(),topImage.getWidth(),topImage.getHeight());
        
        // Get the Graphics2D context
        Graphics2D g2d = resultImage.createGraphics();

        // Draw the base image
        g2d.drawImage(baseImage, 0, 0, null);

        // Draw the top image (with transparency)
        g2d.drawImage(topImage, pos[0], pos[1], null);

        // Dispose of the Graphics2D context
        g2d.dispose();

        return resultImage;
    }
	
	private int[] getQRCodeCord(int bix, int biy, int qrx, int qry) {
		int[] pos = new int[2];		
		int posy = (biy-qry)/2;		
		return new int[]{posy,posy};
	}

	private void createQRImage(String data, String[] textMsgs, String imgPath, int width, int height) throws WriterException, IOException {
		try {
	        QRCodeWriter qrCodeWriter = new QRCodeWriter();
	        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);

	        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
	        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
	        byte[] pngData = pngOutputStream.toByteArray();

	        if (textMsgs.length > 0) {
	            int totalTextLineToadd = textMsgs.length;
	            InputStream in = new ByteArrayInputStream(pngData);
	            BufferedImage image = ImageIO.read(in);

	            BufferedImage outputImage = new BufferedImage(image.getWidth(), image.getHeight() + 25 * totalTextLineToadd, BufferedImage.TYPE_INT_ARGB);
	            Graphics g = outputImage.getGraphics();
	            g.setColor(Color.WHITE);
	            g.fillRect(0, 0, outputImage.getWidth(), outputImage.getHeight());
	            g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
	            g.setFont(new Font("Arial", Font.BOLD, 18));
	            Color textColor = Color.MAGENTA;
	            g.setColor(textColor);
	            FontMetrics fm = g.getFontMetrics();
	            int startingYposition = height + 5;
	            for(String displayText : textMsgs) {
	                g.drawString(displayText, (outputImage.getWidth()/2) - (fm.stringWidth(displayText) / 2), startingYposition);
	                startingYposition += 20;
	            }
	            //ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            ImageIO.write(outputImage, "PNG", new File(imgPath));
	            //baos.flush();
	            //pngData = baos.toByteArray();
	            //baos.close();	            
	        }	        
	    } catch (WriterException | IOException ex) {
	       ex.printStackTrace();
	    }
    }	        
    
	
	private void createQRImage1(String data, String imgPath, int width, int height) throws WriterException, IOException {
		Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height, hints);

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        //ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //ImageIO.write(bufferedImage, "png", baos);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", Path.of(imgPath));        
    }
	
	/*
	public byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageConfig con = new MatrixToImageConfig( 0xFF000002 , 0xFFFFC041 ) ;

        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream,con);
        byte[] pngData = pngOutputStream.toByteArray();
        return pngData;
    }
	*/
	
	private WebClient getQrServiceClient() {
	    return WebClient.builder()
                .baseUrl(qrserviceUrl) // Set the base URL
                .defaultCookie("cookie-name", "cookie-value") // Set default cookies
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) // Set default headers
                .build();	    
	}
}
