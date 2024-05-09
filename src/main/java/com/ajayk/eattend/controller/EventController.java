package com.ajayk.eattend.controller;

import java.io.IOException;

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
import com.ajayk.eattend.model.Contact;
import com.ajayk.eattend.model.Event;
import com.ajayk.eattend.service.EventService;
import com.ajayk.eattend.service.QRCodeService;

@RestController
@RequestMapping("/api/event")
public class EventController {
	
	@Autowired
	EventService eventService;
	
	@Autowired
	QRCodeService qrcodeService;
	
	@GetMapping
	public ResponseEntity<StatusObject> getEvents() {
		ResponseEntity response = new ResponseEntity(HttpStatus.OK);
		StatusObject statusObject = new StatusObject.Builder()
		        .setMessage("Success")
		        .setHttpStatus(HttpStatus.OK.value())
		        .setData(eventService.getAllEvents())
		        .build();
		return response.ok(statusObject);
	}
	
	@GetMapping("/{eventId}/getContacts")
	public ResponseEntity<StatusObject> getContactByEventId(@PathVariable Integer eventId) {
		ResponseEntity response = new ResponseEntity(HttpStatus.OK);
		StatusObject statusObject = new StatusObject.Builder()
		        .setMessage("Success")
		        .setHttpStatus(HttpStatus.OK.value())
		        .setData(eventService.getContactsByEventId(eventId))
		        .build();
		return response.ok(statusObject);
	}
	
	@PostMapping
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
			        .setData(eventService.saveEvent(event))
			        .build();
			return response.ok(statusObject);
		}		
	}
	
	@PostMapping("/{eventId}/importContacts")
	public ResponseEntity<StatusObject> importContacts(@PathVariable Integer eventId, @RequestParam("file") MultipartFile datafile)
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
	
	@PostMapping("/{eventId}/addContact")
	public ResponseEntity<StatusObject> addContacts(@PathVariable Integer eventId, @RequestBody Contact contact)
		throws IOException{		
		ResponseEntity response = new ResponseEntity(HttpStatus.OK);
		if(ObjectUtils.isEmpty(contact)) {
			StatusObject statusObject = new StatusObject.Builder()
			        .setMessage("Contact data not available")
			        .setHttpStatus(HttpStatus.OK.value())
			        .build();
			return response.ok(statusObject);
		}else {
			contact.setEventId(eventId);
			Contact dbObject = eventService.addContact(contact);
			if(ObjectUtils.isEmpty(dbObject)) {
				StatusObject statusObject = new StatusObject.Builder()
				        .setMessage("Object not saved.")
				        .setHttpStatus(HttpStatus.UNPROCESSABLE_ENTITY.value())
				        .build();
				return response.ok(statusObject);
			}else {
				StatusObject statusObject = new StatusObject.Builder()
				        .setMessage("Success")
				        .setHttpStatus(HttpStatus.UNPROCESSABLE_ENTITY.value())
				        .setData(dbObject)
				        .build();
				return response.ok(statusObject);
			}
			
		}		
	}
	
}
