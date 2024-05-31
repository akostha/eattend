package com.ajayk.eattend.dto;

import java.util.List;

import com.ajayk.eattend.model.Contact;
import com.ajayk.eattend.model.Event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QRCodeRequest {
	
	private Integer templateId;
	private Event event;
	private List<Contact> contacts; 

}
