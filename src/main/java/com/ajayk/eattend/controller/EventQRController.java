package com.ajayk.eattend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.ajayk.eattend.service.QRCodeService;

@RestController
public class EventQRController {
	
	@Autowired
	QRCodeService qrcodeService;
	
	
}
