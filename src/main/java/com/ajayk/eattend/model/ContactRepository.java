package com.ajayk.eattend.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long>{
	
	public List<Contact> findByEventId(int eventId);

}
