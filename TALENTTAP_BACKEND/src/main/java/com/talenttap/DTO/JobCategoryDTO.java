package com.talenttap.DTO;

import com.talenttap.entity.JobCategory;

public class JobCategoryDTO {
	private int jobCategoryId;
	private String jobCategory;
	
	public JobCategoryDTO(JobCategory jobCategory) {
		this.jobCategoryId = jobCategory.getJobCategoryId();
		this.jobCategory = jobCategory.getJobCategory();
		
	}
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
