package com.talenttap.DTO;

import com.talenttap.entity.EmploymentType;

public class EmploymentTypeDTO {
	private int employmentTypeId;
	private String employmentType;
	
	
	public EmploymentTypeDTO(EmploymentType skill) {
		super();
		this.employmentTypeId = skill.getEmploymentTypeId();
		this.employmentType = skill.getEmploymentType();
	}

	public EmploymentTypeDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

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
