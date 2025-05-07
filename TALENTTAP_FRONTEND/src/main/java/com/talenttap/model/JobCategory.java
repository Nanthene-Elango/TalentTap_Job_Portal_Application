package com.talenttap.model;

import lombok.Data;

@Data
public class JobCategory {
	private int jobCategoryId;
	private String jobCategory;
	
	public int getJobCategoryId() {
		return jobCategoryId;
	}
	public void setJobCategoryId(int jobCategoryId) {
		this.jobCategoryId = jobCategoryId;
	}
	public String getJobCategory() {
		return jobCategory;
	}
	public void setJobCategory(String jobCategory) {
		this.jobCategory = jobCategory;
	}
	
	
}
