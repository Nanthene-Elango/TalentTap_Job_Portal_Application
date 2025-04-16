package com.talenttap.DTO;

import com.talenttap.entity.IndustryType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class IndustryTypeDTO {
	private int industryTypeId;
	private String industryType;
	
	public IndustryTypeDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IndustryTypeDTO(IndustryType industryType) {
		super();
		this.industryTypeId = industryType.getIndustryTypeId();
		this.industryType = industryType.getIndustryType();
	}

	public int getIndustryTypeId() {
		return industryTypeId;
	}

	public void setIndustryTypeId(int industryTypeId) {
		this.industryTypeId = industryTypeId;
	}

	public String getIndustryType() {
		return industryType;
	}

	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}
	
	
}
