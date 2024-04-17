package com.ajayk.eattend.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajayk.eattend.dto.QRCodeRequest;
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

@Service
public class QRCodeService {
	
	@Autowired
	EventRepository eventRepository;
	
	@Autowired
	ContactRepository contactRepository;
	
	@Autowired
	ImagePathService pathService;
	
	private int imageWidth = 400;
	private int imageHeight = 300;
	
	public void createQRCode(QRCodeRequest qrcodeRequest) throws WriterException, IOException {
		
		Optional<Event> obj = eventRepository.findById(Long.valueOf(qrcodeRequest.getEventid()));
		List<Contact> contacts =  contactRepository.findByEventId(qrcodeRequest.getEventid());
		
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
}
