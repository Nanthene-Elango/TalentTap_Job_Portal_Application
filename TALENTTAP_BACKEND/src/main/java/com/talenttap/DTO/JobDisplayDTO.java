package com.talenttap.DTO;

import com.talenttap.entity.Jobs;
import com.talenttap.entity.Location;
import com.talenttap.entity.Skills;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
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
}