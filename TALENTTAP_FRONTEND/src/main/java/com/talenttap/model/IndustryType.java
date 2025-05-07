package com.talenttap.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class IndustryType {
	private Integer industryTypeId;
	private String industryType;
	
	public Integer getIndustryTypeId() {
		return industryTypeId;
	}
	public void setIndustryTypeId(Integer industryTypeId) {
		this.industryTypeId = industryTypeId;
	}
	public String getIndustryType() {
		return industryType;
	}
	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}
	
}
