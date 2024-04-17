package com.ajayk.eattend.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ajayk.eattend.dto.StatusObject;
import com.ajayk.eattend.model.Event;
import com.ajayk.eattend.model.EventRepository;
import com.ajayk.eattend.service.EventService;
import com.ajayk.eattend.service.QRCodeService;

@RestController
@RequestMapping("/api")
public class EventController {
	
	@Autowired
	EventRepository eventRepository;
	
	@Autowired
	EventService eventService;
	
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
		ResponseEntity response = new ResponseEntity(HttpStatus.OK);
		if(ObjectUtils.isEmpty(event)) {
			StatusObject statusObject = new StatusObject.Builder()
			        .setMessage("Event data not available")
			        .setHttpStatus(HttpStatus.OK.value())
			        .build();
			return response.ok(statusObject);
		}else {
			StatusObject statusObject = new StatusObject.Builder()
			        .setMessage("Success")
			        .setHttpStatus(HttpStatus.OK.value())
			        .setData(eventRepository.save(event))
			        .build();
			return response.ok(statusObject);
		}		
	}
	
	@PostMapping("/event/{eventId}/addContacts")
	public ResponseEntity<StatusObject> addContacts(@PathVariable Integer eventId, @RequestParam("file") MultipartFile datafile)
		throws IOException{		
		ResponseEntity response = new ResponseEntity(HttpStatus.OK);
		if(ObjectUtils.isEmpty(eventId)) {
			StatusObject statusObject = new StatusObject.Builder()
			        .setMessage("Event data not available")
			        .setHttpStatus(HttpStatus.OK.value())
			        .build();
			return response.ok(statusObject);
		}else if(!datafile.getContentType().equals("text/csv")) {
			StatusObject statusObject = new StatusObject.Builder()
			        .setMessage("File is not a csv  file.")
			        .setHttpStatus(HttpStatus.OK.value())
			        .build();
			return response.ok(statusObject);
		}
		else {
			eventService.processCsvFile(datafile, eventId);
			StatusObject statusObject = new StatusObject.Builder()
			        .setMessage("Success")
			        .setHttpStatus(HttpStatus.OK.value())
			        .build();
			return response.ok(statusObject);
		}		
	}
	
	
}
