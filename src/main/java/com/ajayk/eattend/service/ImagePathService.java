package com.ajayk.eattend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ajayk.eattend.model.Contact;
import com.ajayk.eattend.model.ContactRepository;
import com.ajayk.eattend.model.Event;

@Service
public class ImagePathService {
	
	@Value("${images.basepath}")
	private String basePath;
	
	@Autowired
	ContactRepository contactRepository;
	
	public String getImagePath(Event event, Contact contact) {
		String nameSeperator = "_";
		String pathStr = basePath + event.getId() + nameSeperator + contact.getFirstName() + ".png";
		return pathStr;
	}

}
