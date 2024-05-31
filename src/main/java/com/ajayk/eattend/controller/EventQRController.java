package com.ajayk.eattend.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajayk.eattend.dto.QRCodeRequest;
import com.ajayk.eattend.dto.StatusObject;
import com.ajayk.eattend.service.QRCodeService;
import com.google.zxing.WriterException;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/eattend")
@Slf4j
public class EventQRController {
	
	@Autowired
	QRCodeService qrcodeService;
	
	@PostMapping("/qrcode")
	public Mono<StatusObject> createQrCode(@RequestBody QRCodeRequest body) throws WriterException, IOException{
		log.info("Method {} is called", "EventQRController.createQrCode");		
		Mono<StatusObject> result = qrcodeService.callQRCodeService(body);
		/*
		final ResponseEntity<Mono<StatusObject>> rEntity = new ResponseEntity<Mono<StatusObject>>(HttpStatus.OK);
		result.subscribe(elem -> {
			StatusObject statusObject = new StatusObject.Builder()
		        .setHttpStatus(HttpStatus.OK)
		        .setData(elem.getData())
		        .build();
			rEntity.ok(statusObject);
				},
			    error -> {
			        log.error(error.getStackTrace().toString());
			        StatusObject statusObject = new StatusObject.Builder()
					        .setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
					        .setMessage(error.getMessage())
					        .build();
						rEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			    },
			    () -> {
			        // Handle completion (optional)
			    	StatusObject statusObject = new StatusObject.Builder()
					        .setHttpStatus(HttpStatus.OK)
					        .setMessage("No data Found")
					        .build();
						rEntity.ok(statusObject);
						log.info("Completed without a value");
			    }
	     	);
	     	*/
		return result;
		
	}
	
	@PostMapping("/testqrcode")
	public ResponseEntity<StatusObject> createTestQRCode(@RequestBody QRCodeRequest body) throws WriterException, IOException{	
		log.info("Method {} is called", "EventQRController.createTestQRCode");
		qrcodeService.createTestQRCode(body);
		StatusObject statusObject = new StatusObject.Builder()
		        .setMessage("Success")
		        .setHttpStatus(HttpStatus.OK)
		        //.setData(eventRepository.findAll())
		        .build();
		return ResponseEntity.ok(statusObject);
		
	}
}
