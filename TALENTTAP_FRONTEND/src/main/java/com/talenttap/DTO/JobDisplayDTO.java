package com.talenttap.DTO;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.Data;

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
}
