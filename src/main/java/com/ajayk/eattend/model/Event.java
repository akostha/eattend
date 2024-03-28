package com.ajayk.eattend.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "event", schema="devldb")
public class Event {
	
	@Id
	private int id;
	@Column(name = "descr")
	private String descr;	
	@Column(name = "event_date")
	private Date eventDate;

}
