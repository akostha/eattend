package com.ajayk.eattend.dto;

import com.ajayk.eattend.model.Contact;
import com.ajayk.eattend.model.Event;

import lombok.Data;

@Data
public class QRCodeInfo {
	
	private Event event;
	private Contact contact;

}
