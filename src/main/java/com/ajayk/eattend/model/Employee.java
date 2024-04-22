package com.ajayk.eattend.model;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee", schema="devldb")
public class Employee {
	
	@Id
	private int id;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	@Column(name = "salary")
	private int salary;
	@Column(name = "dept")
	private String dept;
	@Column(name = "createdat")
	private LocalDateTime createdAt;
	
	public Employee() {
		this.createdAt = LocalDateTime.now();
	}
	
	public Employee(int id, String firstName, String lastName, int salary, String dept) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.salary = salary;
		this.dept = dept;
		this.createdAt = LocalDateTime.now();
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public int getSalary() {
		return salary;
	}
	public void setSalary(int salary) {
		this.salary = salary;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String toString() {
		return String.format("Employee {id=%d, firstName=%s, lastName=%s, dept=%s, salary=%d}", 
				id, firstName, lastName, dept, salary);		
	}

}
