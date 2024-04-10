package com.ajayk.eattend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajayk.eattend.dto.StatusObject;
import com.ajayk.eattend.model.Contact;
import com.ajayk.eattend.model.ContactRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class ContactController {

	@Autowired
	ContactRepository contactRepository;
	
	@GetMapping("/contact")
	public ResponseEntity<StatusObject> getEmployees() {
		ResponseEntity response = new ResponseEntity(HttpStatus.OK);
		StatusObject statusObject = new StatusObject.Builder()
		        .setMessage("Success")
		        .setHttpStatus(HttpStatus.OK.value())
		        .setData(contactRepository.findAll())
		        .build();
		return response.ok(statusObject);
	}
	
	@PostMapping("/contact")
	public ResponseEntity<StatusObject> createContact(@RequestBody Contact body) {
		Contact object = contactRepository.save(body);
		
		ResponseEntity response = new ResponseEntity(HttpStatus.OK);
		StatusObject statusObject = new StatusObject.Builder()
		        .setMessage("Success")
		        .setHttpStatus(HttpStatus.OK.value())
		        .setData(object.getId())
		        .build();
		return response.ok(statusObject);
	}
}
