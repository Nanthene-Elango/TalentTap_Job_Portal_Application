package com.talenttap.model;

import java.time.LocalDate;


public class Experience {

	private Integer id;
	private String role;
	private String company;
	private String employment;
	private Integer employmentId;
	private LocalDate start;
	private LocalDate end;
	private String description;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	
	public String getEmployment() {
		return employment;
	}
	public void setEmployment(String employment) {
		this.employment = employment;
	}
	public Integer getEmploymentId() {
		return employmentId;
	}
	public void setEmploymentId(Integer employmentId) {
		this.employmentId = employmentId;
	}
	public LocalDate getStart() {
		return start;
	}
	public void setStart(LocalDate start) {
		this.start = start;
	}
	public LocalDate getEnd() {
		return end;
	}
	public void setEnd(LocalDate end) {
		this.end = end;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
