package com.ajayk.eattend.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "event", schema="eventsdb")
@Data
public class Event {
	
	@Id
	private int id;
	@Column(name = "descr")
	private String descr;	
	@Column(name = "event_date")
	private LocalDate eventDate;
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

}
