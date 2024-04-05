package com.ajayk.eattend.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ajayk.eattend.dto.QRCodeRequest;
import com.ajayk.eattend.dto.StatusObject;
import com.ajayk.eattend.service.QRCodeService;
import com.google.zxing.WriterException;

@RestController("/api")
public class EventQRController {
	
	@Autowired
	QRCodeService qrcodeService;
	
	@PostMapping("/qrcode")
	public ResponseEntity<StatusObject> createQrCode(@RequestBody QRCodeRequest body) throws WriterException, IOException{		
		qrcodeService.createQRCode("ajayk", 400, 300);
		ResponseEntity response = new ResponseEntity(HttpStatus.OK);
		StatusObject statusObject = new StatusObject.Builder()
		        .setMessage("Success")
		        .setHttpStatus(HttpStatus.OK.value())
		        //.setData(eventRepository.findAll())
		        .build();
		return response.ok(statusObject);
		
	}
}
