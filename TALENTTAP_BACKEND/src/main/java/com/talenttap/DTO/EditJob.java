package com.talenttap.DTO;

import java.time.LocalDate;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EditJob {
	private Integer jobId;
	
	@NotBlank(message = "Job role is required")
    @Size(min = 3, message = "Job role must be at least 3 characters")
    private String jobRole;

    @NotNull(message = "Job type ID is required")
    private Integer jobTypeId;

    @NotNull(message = "Job category ID is required")
    private Integer jobCategoryId;

    @NotBlank(message = "Job description is required")
    @Size(min = 100, max = 3000, message = "Description must be between 100 and 3000 characters")
    private String jobDescription;

    @NotNull(message = "Skill IDs are required")
    private List<Integer> skillIds;

    @NotBlank(message = "Responsibilities are required")
    private String responsibilities;

    private String requirements;
    private String benefits;

    @NotBlank(message = "Work type is required")
    private String workType;

    @NotNull(message = "Location IDs are required")
    private List<Integer> locationIds;

    private String yearsOfExperience;
    private Double salaryMin;
    private Double salaryMax;
    private Integer duration;
    private Double stipend;
    private Integer openings;
    private LocalDate deadline;

	// Getters and Setters
    public Integer getJobId() {
		return jobId;
	}
	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}

    public String getJobRole() { return jobRole; }
    public void setJobRole(String jobRole) { this.jobRole = jobRole; }
    public Integer getJobTypeId() { return jobTypeId; }
    public void setJobTypeId(Integer jobTypeId) { this.jobTypeId = jobTypeId; }
    public Integer getJobCategoryId() { return jobCategoryId; }
    public void setJobCategoryId(Integer jobCategoryId) { this.jobCategoryId = jobCategoryId; }
    public String getJobDescription() { return jobDescription; }
    public void setJobDescription(String jobDescription) { this.jobDescription = jobDescription; }
    public List<Integer> getSkillIds() { return skillIds; }
    public void setSkillIds(List<Integer> skillIds) { this.skillIds = skillIds; }
    public String getResponsibilities() { return responsibilities; }
    public void setResponsibilities(String responsibilities) { this.responsibilities = responsibilities; }
    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }
    public String getBenefits() { return benefits; }
    public void setBenefits(String benefits) { this.benefits = benefits; }
    public String getWorkType() { return workType; }
    public void setWorkType(String workType) { this.workType = workType; }
    public List<Integer> getLocationIds() { return locationIds; }
    public void setLocationIds(List<Integer> locationIds) { this.locationIds = locationIds; }
    public String getYearsOfExperience() { return yearsOfExperience; }
    public void setYearsOfExperience(String yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; }
    public Double getSalaryMin() { return salaryMin; }
    public void setSalaryMin(Double salaryMin) { this.salaryMin = salaryMin; }
    public Double getSalaryMax() { return salaryMax; }
    public void setSalaryMax(Double salaryMax) { this.salaryMax = salaryMax; }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    public Double getStipend() { return stipend; }
    public void setStipend(Double stipend) { this.stipend = stipend; }
    public Integer getOpenings() { return openings; }
    public void setOpenings(Integer openings) { this.openings = openings; }
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }   
}
