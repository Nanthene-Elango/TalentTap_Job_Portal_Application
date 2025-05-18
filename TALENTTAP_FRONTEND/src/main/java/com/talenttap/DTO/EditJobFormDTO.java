package com.talenttap.DTO;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EditJobFormDTO {
	   private int jobId;
	  public int getJobId() {
		return jobId;
	}
	public void setJobId(int jobId) {
		this.jobId = jobId;
	}
	@NotBlank
	    @Size(min = 3, max = 100)
	    private String jobRole;

	    @NotNull
	    private Integer jobTypeId;

	    @NotNull
	    private int jobCategoryId;

	    @NotBlank
	    @Size(min = 100, max = 3000)
	    private String jobDescription;

	    @NotEmpty
	    private List<Long> skillIds;

	    @NotBlank
	    private String responsibilities;

	    @NotBlank
	    private String requirements;

	    private String benefits;

	    @NotBlank
	    private String workType;

	    @NotEmpty
	    private List<Long> locationIds;

	    private String yearsOfExperience;

	    @Min(0)
	    private Double salaryMin;

	    @Min(0)
	    private Double salaryMax;

	    @Min(1)
	    @Max(24)
	    private Integer duration;

	    @Min(0)
	    private Double stipend;

	    @Min(1)
	    private Integer openings;

	    @NotNull
	    @Future
	    private String deadline;

	    // Getters and setters
	    public String getJobRole() { return jobRole; }
	    public void setJobRole(String jobRole) { this.jobRole = jobRole; }
	    public @NotNull Integer getJobTypeId() { return jobTypeId; }
	    public void setJobTypeId(Integer jobTypeId2) { this.jobTypeId = jobTypeId2; }
	    public Integer getJobCategoryId() { return jobCategoryId; }
	    public void setJobCategoryId(Integer jobCategoryId) { this.jobCategoryId = jobCategoryId; }
	    public String getJobDescription() { return jobDescription; }
	    public void setJobDescription(String jobDescription) { this.jobDescription = jobDescription; }
	    public List<Long> getSkillIds() { return skillIds; }
	    public void setSkillIds(List<Long> skillIds) { this.skillIds = skillIds; }
	    public String getResponsibilities() { return responsibilities; }
	    public void setResponsibilities(String responsibilities) { this.responsibilities = responsibilities; }
	    public String getRequirements() { return requirements; }
	    public void setRequirements(String requirements) { this.requirements = requirements; }
	    public String getBenefits() { return benefits; }
	    public void setBenefits(String benefits) { this.benefits = benefits; }
	    public String getWorkType() { return workType; }
	    public void setWorkType(String workType) { this.workType = workType; }
	    public List<Long> getLocationIds() { return locationIds; }
	    public void setLocationIds(List<Long> locationIds) { this.locationIds = locationIds; }
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
	    public String getDeadline() { return deadline; }
	    public void setDeadline(String deadline) { this.deadline = deadline; }

}
