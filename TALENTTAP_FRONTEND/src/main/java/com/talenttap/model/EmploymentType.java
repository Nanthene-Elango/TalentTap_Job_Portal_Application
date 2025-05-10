package com.talenttap.model;

import lombok.Data;

public class EmploymentType {
	private int employmentTypeId;
	private String employmentType;
	public int getEmploymentTypeId() {
		return employmentTypeId;
	}
	public void setEmploymentTypeId(int employmentTypeId) {
		this.employmentTypeId = employmentTypeId;
	}
	public String getEmploymentType() {
		return employmentType;
	}
	public void setEmploymentType(String employmentType) {
		this.employmentType = employmentType;
	}
	
	
}
