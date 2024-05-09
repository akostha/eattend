package com.ajayk.eattend.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ajayk.eattend.model.Contact;
import com.ajayk.eattend.model.ContactRepository;
import com.ajayk.eattend.model.Event;
import com.ajayk.eattend.model.EventRepository;

@Service
public class EventService {
	
	@Autowired
	EventRepository eventRepository;
	
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

	public Event saveEvent(Event event) {
		return eventRepository.save(event);
	}
	
	
	public List<Event> getAllEvents() {		
		return eventRepository.findAll();		
	}
	
	public List<Contact> getContactsByEventId(Integer eventId) {		
		if(!ObjectUtils.isEmpty(eventId)){
			return contactRepository.findByEventId(eventId);
		}
		return null;
	}

	public Contact addContact(Contact contact) {		
		if(ObjectUtils.isEmpty(contact.getId())){
			return contactRepository.save(contact);
		}
		return null;
	}
	
}
