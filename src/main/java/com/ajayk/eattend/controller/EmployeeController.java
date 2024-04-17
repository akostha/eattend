package com.ajayk.eattend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajayk.eattend.dto.StatusObject;
import com.ajayk.eattend.model.EmployeeRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class EmployeeController {
	
	@Autowired
	EmployeeRepository empRepository;
	
	@GetMapping("/emp")
	public ResponseEntity<StatusObject> getEmployees() {
		ResponseEntity response = new ResponseEntity(HttpStatus.OK);
		StatusObject statusObject = new StatusObject.Builder()
		        .setMessage("Success")
		        .setHttpStatus(HttpStatus.OK.value())
		        .setData(empRepository.findAll())
		        .build();
		return response.ok(statusObject);
	}

}
