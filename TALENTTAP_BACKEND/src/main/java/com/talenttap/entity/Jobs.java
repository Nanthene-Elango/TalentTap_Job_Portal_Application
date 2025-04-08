package com.talenttap.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Jobs {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "job_id")
	private int jobId;
	
	@ManyToOne
	@JoinColumn(name = "employer_id" , nullable = false)
	private Employer employer;
	
	@Column(name = "job_role" , nullable = false)
	private String jobRole;
	
	@ManyToOne
	@JoinColumn(name = "job_type" , nullable = false)
	private EmploymentType jobType;
	
	@ManyToOne
	@JoinColumn(name= "job_category_id" , nullable = false)
	private JobCategory jobCategory;
	
	@Column(name = "job_description" , nullable = false , columnDefinition = "TEXT")
	private String jobDescription;
	
	@Column(name = "roles" , columnDefinition = "TEXT")
	private String roles;
	
	@Column(name = "responsibilities", columnDefinition = "TEXT")
	private String responsibilities;
	
	@Column(name = "benefits" , columnDefinition = "TEXT")
	private String benefits;
	
	@Column(name = "duration")
	private int duration;
	
	@Column(name = "stipend")
	private double stipend;
	
	@ManyToOne
	@JoinColumn(name="salary_range_id")
	private SalaryRange salary_range;
	
	@Column(name= "years_of_experience")
	private String yearsOfExperience;
	
	@Enumerated(EnumType.STRING)
	@Column(name="work_type")
	private WorkType workType;
	
	@Column(name="openings")
	private int openings;
	
	@Column(name="posted_date" , nullable=false)
	private LocalDateTime postedDate;
	
	@Column(name="deadline" , nullable = false)
	private LocalDateTime deadline;
	
	@Enumerated(EnumType.STRING)
	@Column(name="job_status" , nullable = false)
	private JobStatus jobStatus = JobStatus.open;
	
	@Enumerated(EnumType.STRING)
	@Column(name="approval_status" , nullable = false)
	private ApplicationStatus approvalStatus;
	
	@ManyToMany
	@JoinTable(
	    name = "required_skills", 
	    joinColumns = @JoinColumn(name = "job_id"), 
	    inverseJoinColumns = @JoinColumn(name = "skill_id")
	)
	private Set<Skills> requiredSkills = new HashSet<>();

	@ManyToMany
	@JoinTable(
	    name = "job_location", 
	    joinColumns = @JoinColumn(name = "job_id"), 
	    inverseJoinColumns = @JoinColumn(name = "location_id")
	)
	private Set<Location> jobLocation = new HashSet<>();
}
