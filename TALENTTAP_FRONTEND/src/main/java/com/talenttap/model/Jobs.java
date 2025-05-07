package com.talenttap.model;

import java.time.LocalDate;
import java.util.Set;

public class Jobs {

	private int jobId;
	private String company;
	private Set<Skills> skills;
	private String jobTitle;
	private String jobCategory;
	private Set<String> location;
	private String salaryRange;
	private int openings;
	private LocalDate postedDate;
	private LocalDate deadline;
	private String timeleft;
	private String postedAgo;
	private int applicants;
	private String jobType;
	private String description;
	private String roles;
	private String responsibilities;
	private String benefits;
	private Integer duration;
	private Double stipend;
	
	public int getJobId() {
		return jobId;
	}
	public void setJobId(int jobId) {
		this.jobId = jobId;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public Set<Skills> getSkills() {
		return skills;
	}
	public void setSkills(Set<Skills> skills) {
		this.skills = skills;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getJobCategory() {
		return jobCategory;
	}
	public void setJobCategory(String jobCategory) {
		this.jobCategory = jobCategory;
	}
	public Set<String> getLocation() {
		return location;
	}
	public void setLocation(Set<String> location) {
		this.location = location;
	}
	public String getSalaryRange() {
		return salaryRange;
	}
	public void setSalaryRange(String salaryRange) {
		this.salaryRange = salaryRange;
	}
	public int getOpenings() {
		return openings;
	}
	public void setOpenings(int openings) {
		this.openings = openings;
	}
	public LocalDate getPostedDate() {
		return postedDate;
	}
	public void setPostedDate(LocalDate postedDate) {
		this.postedDate = postedDate;
	}
	public LocalDate getDeadline() {
		return deadline;
	}
	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}
	public String getTimeleft() {
		return timeleft;
	}
	public void setTimeleft(String timeleft) {
		this.timeleft = timeleft;
	}
	public int getApplicants() {
		return applicants;
	}
	public void setApplicants(int applicants) {
		this.applicants = applicants;
	}
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRoles() {
		return roles;
	}
	public void setRoles(String roles) {
		this.roles = roles;
	}
	public String getResponsibilities() {
		return responsibilities;
	}
	public void setResponsibilities(String responsibilities) {
		this.responsibilities = responsibilities;
	}
	public String getBenefits() {
		return benefits;
	}
	public void setBenefits(String benefits) {
		this.benefits = benefits;
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
	public String getPostedAgo() {
		return postedAgo;
	}
	public void setPostedAgo(String postedAgo) {
		this.postedAgo = postedAgo;
	}

}
