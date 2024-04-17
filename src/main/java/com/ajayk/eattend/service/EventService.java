package com.ajayk.eattend.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ajayk.eattend.model.Contact;
import com.ajayk.eattend.model.ContactRepository;

@Service
public class EventService {
	
	@Autowired
	ContactRepository contactRepository;
	
	
	public void processCsvFile(MultipartFile file, Integer eventId) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            reader.readLine(); //read header line
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 4) {
                    Contact contact = new Contact();
                    contact.setFirstName(fields[0]);
                    contact.setLastName(fields[1]);
                    contact.setEmail(fields[2]);
                    contact.setPhoneNo(fields[3]);
                    contact.setEventId(eventId);
                    contactRepository.save(contact);
                }
            }
        }
	}
	
}
