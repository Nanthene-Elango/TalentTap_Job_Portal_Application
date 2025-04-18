package com.talenttap.DTO;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public class JobFormDTO {
    @NotBlank
    @Size(min = 3, max = 100)
    private String jobRole;

    @NotNull
    private Long jobTypeId;

    @NotNull
    private Long jobCategoryId;

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
    private LocalDate deadline;

    // Getters and setters
    public String getJobRole() { return jobRole; }
    public void setJobRole(String jobRole) { this.jobRole = jobRole; }
    public Long getJobTypeId() { return jobTypeId; }
    public void setJobTypeId(Long jobTypeId) { this.jobTypeId = jobTypeId; }
    public Long getJobCategoryId() { return jobCategoryId; }
    public void setJobCategoryId(Long jobCategoryId) { this.jobCategoryId = jobCategoryId; }
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
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
}