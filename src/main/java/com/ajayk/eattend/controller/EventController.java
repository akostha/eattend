package com.ajayk.eattend.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajayk.eattend.dto.StatusObject;
import com.ajayk.eattend.model.Event;
import com.ajayk.eattend.model.EventRepository;
import com.ajayk.eattend.service.QRCodeService;
import com.google.zxing.WriterException;

@RestController
@RequestMapping("/api")
public class EventController {
	
	@Autowired
	EventRepository eventRepository;
	
	@Autowired
	QRCodeService qrcodeService;
	
	@GetMapping("/event")
	public ResponseEntity<StatusObject> getEmployees() {
		ResponseEntity response = new ResponseEntity(HttpStatus.OK);
		StatusObject statusObject = new StatusObject.Builder()
		        .setMessage("Success")
		        .setHttpStatus(HttpStatus.OK.value())
		        .setData(eventRepository.findAll())
		        .build();
		return response.ok(statusObject);
	}
	
	@PostMapping("/event")
	public ResponseEntity<StatusObject> createEvent(@RequestBody Event event){		
		if(ObjectUtils.isEmpty(event)) {
			ResponseEntity response = new ResponseEntity(HttpStatus.OK);
			StatusObject statusObject = new StatusObject.Builder()
			        .setMessage("Event data not available")
			        .setHttpStatus(HttpStatus.OK.value())
			        .build();
			return response.ok(statusObject);
		}else {
			event.setCreatedAt(LocalDateTime.now());
			event.setEventDate(LocalDate.now().plusDays(10));
			ResponseEntity response = new ResponseEntity(HttpStatus.OK);
			StatusObject statusObject = new StatusObject.Builder()
			        .setMessage("Success")
			        .setHttpStatus(HttpStatus.OK.value())
			        .setData(eventRepository.save(event))
			        .build();
			return response.ok(statusObject);
		}
		
	}
	
	@PostMapping("/event/qrcode")
	public ResponseEntity<StatusObject> createQrCode() throws WriterException, IOException{		
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