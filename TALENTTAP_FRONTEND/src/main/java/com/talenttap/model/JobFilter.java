package com.talenttap.model;

public class JobFilter {
	
	private String jobType;
	private Integer location;
	private String experience;
	private Integer category;
	private String salary;
	private Integer freshness;
	private Integer duration;
	private Double stipend;
	
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	public Integer getLocation() {
		return location;
	}
	public void setLocation(Integer location) {
		this.location = location;
	}
	public String getExperience() {
		return experience;
	}
	public void setExperience(String experience) {
		this.experience = experience;
	}
	public Integer getCategory() {
		return category;
	}
	public void setCategory(Integer category) {
		this.category = category;
	}
	public String getSalary() {
		return salary;
	}
	public void setSalary(String salary) {
		this.salary = salary;
	}
	public Integer getFreshness() {
		return freshness;
	}
	public void setFreshness(Integer freshness) {
		this.freshness = freshness;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public Double getStipend() {
		return stipend;
	}
	public void setStipend(Double stipend) {
		this.stipend = stipend;
	}

}
