package com.talenttap.model;

public class EmployerJobFilter {
	// frontend 
	private String workType; // internship, fulltime
	private String jobStatus;
	private Integer employmentType; // remote, hybrid, onsite
	private Integer location;
	private String keyword;
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getWorkType() {
		return workType;
	}
	public void setWorkType(String jobType) {
		this.workType = jobType;
	}
	public String getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	public Integer getEmploymentType() {
		return employmentType;
	}
	public void setEmploymentType(Integer employementType) {
		this.employmentType = employementType;
	}
	public Integer getLocation() {
		return location;
	}
	public void setLocation(Integer location) {
		this.location = location;
	}
}
