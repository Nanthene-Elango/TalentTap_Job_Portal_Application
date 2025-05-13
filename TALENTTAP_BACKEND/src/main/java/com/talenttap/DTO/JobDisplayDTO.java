package com.talenttap.DTO;

import com.talenttap.entity.Jobs;
import com.talenttap.entity.Location;
import com.talenttap.entity.Skills;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class JobDisplayDTO {
    private int jobId;
    private String jobRole;
    private String jobType; // e.g., "Full-time", "Internship"
    private String jobCategory;
    private String jobDescription;
    private String roles;
    private String responsibilities;
    private String benefits;
    private Integer duration; // Internship-specific
    private Double stipend; // Internship-specific
    private Double salaryMin; // Full-time/Part-time-specific
    private Double salaryMax; // Full-time/Part-time-specific
    private String yearsOfExperience;
    private String workType; // e.g., "REMOTE", "ONSITE"
    private Integer openings;
    private LocalDateTime postedDate;
    private LocalDateTime deadline;
    private String jobStatus; // e.g., "open", "closing"
    private String approvalStatus; // e.g., "approved", "pending"
    private Set<String> requiredSkills; // Skill names
    private Set<String> locations; // Location names
    private boolean isExpired; // Derived field

    public JobDisplayDTO(Jobs job) {
        this.jobId = job.getJobId();
        this.jobRole = job.getJobRole();
        this.jobType = job.getJobType().getEmploymentType();
        this.jobCategory = job.getJobCategory().getJobCategory();
        this.jobDescription = job.getJobDescription();
        this.roles = job.getRoles();
        this.responsibilities = job.getResponsibilities();
        this.benefits = job.getBenefits();
        this.duration = job.getDuration() != 0 ? job.getDuration() : null;
        this.stipend = job.getStipend() != 0 ? job.getStipend() : null;
        this.salaryMin = job.getSalary_range() != null ? job.getSalary_range().getMin_range() : null;
        this.salaryMax = job.getSalary_range() != null ? job.getSalary_range().getMax_range() : null;
        this.yearsOfExperience = job.getYearsOfExperience();
        this.workType = job.getWorkType().name();
        this.openings = job.getOpenings();
        this.postedDate = job.getPostedDate();
        this.deadline = job.getDeadline();
        this.jobStatus = job.getJobStatus().name();
        this.approvalStatus = job.getApprovalStatus().name();
        this.requiredSkills = job.getRequiredSkills().stream()
                .map(Skills::getSkill)
                .collect(Collectors.toSet());
        this.locations = job.getJobLocation().stream()
                .map(Location::getLocation)
                .collect(Collectors.toSet());
        this.isExpired = job.getDeadline().isBefore(LocalDateTime.now());
    }

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public String getJobRole() {
		return jobRole;
	}

	public void setJobRole(String jobRole) {
		this.jobRole = jobRole;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getJobCategory() {
		return jobCategory;
	}

	public void setJobCategory(String jobCategory) {
		this.jobCategory = jobCategory;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
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

	public Double getSalaryMin() {
		return salaryMin;
	}

	public void setSalaryMin(Double salaryMin) {
		this.salaryMin = salaryMin;
	}

	public Double getSalaryMax() {
		return salaryMax;
	}

	public void setSalaryMax(Double salaryMax) {
		this.salaryMax = salaryMax;
	}

	public String getYearsOfExperience() {
		return yearsOfExperience;
	}

	public void setYearsOfExperience(String yearsOfExperience) {
		this.yearsOfExperience = yearsOfExperience;
	}

	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public Integer getOpenings() {
		return openings;
	}

	public void setOpenings(Integer openings) {
		this.openings = openings;
	}

	public LocalDateTime getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(LocalDateTime postedDate) {
		this.postedDate = postedDate;
	}

	public LocalDateTime getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDateTime deadline) {
		this.deadline = deadline;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public Set<String> getRequiredSkills() {
		return requiredSkills;
	}

	public void setRequiredSkills(Set<String> requiredSkills) {
		this.requiredSkills = requiredSkills;
	}

	public Set<String> getLocations() {
		return locations;
	}

	public void setLocations(Set<String> locations) {
		this.locations = locations;
	}

	public boolean isExpired() {
		return isExpired;
	}

	public void setExpired(boolean isExpired) {
		this.isExpired = isExpired;
	}
   
}