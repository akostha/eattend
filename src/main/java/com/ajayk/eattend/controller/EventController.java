package com.ajayk.eattend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajayk.eattend.dto.StatusObject;
import com.ajayk.eattend.model.EventRepository;

@RestController
@RequestMapping("/api")
public class EventController {
	
	@Autowired
	EventRepository eventRepository;
	
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
	
	@PostMapping("/event/qrcode")
	public ResponseEntity<StatusObject> createQrCode(){		
		ResponseEntity response = new ResponseEntity(HttpStatus.OK);
		StatusObject statusObject = new StatusObject.Builder()
		        .setMessage("Success")
		        .setHttpStatus(HttpStatus.OK.value())
		        //.setData(eventRepository.findAll())
		        .build();
		return response.ok(statusObject);
		
	}
}
