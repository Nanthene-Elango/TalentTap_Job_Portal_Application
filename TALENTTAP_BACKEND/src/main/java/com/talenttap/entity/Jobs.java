package com.talenttap.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Jobs {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "job_id")
	private int jobId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonManagedReference
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
	private Integer duration;
	
	@Column(name = "stipend")
	private Double stipend;
	
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

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public Employer getEmployer() {
		return employer;
	}

	public void setEmployer(Employer employer) {
		this.employer = employer;
	}

	public String getJobRole() {
		return jobRole;
	}

	public void setJobRole(String jobRole) {
		this.jobRole = jobRole;
	}

	public EmploymentType getJobType() {
		return jobType;
	}

	public void setJobType(EmploymentType jobType) {
		this.jobType = jobType;
	}

	public JobCategory getJobCategory() {
		return jobCategory;
	}

	public void setJobCategory(JobCategory jobCategory) {
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

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public double getStipend() {
		return stipend;
	}

	public void setStipend(double stipend) {
		this.stipend = stipend;
	}

	public SalaryRange getSalary_range() {
		return salary_range;
	}

	public void setSalary_range(SalaryRange salary_range) {
		this.salary_range = salary_range;
	}

	public String getYearsOfExperience() {
		return yearsOfExperience;
	}

	public void setYearsOfExperience(String yearsOfExperience) {
		this.yearsOfExperience = yearsOfExperience;
	}

	public WorkType getWorkType() {
		return workType;
	}

	public void setWorkType(WorkType workType) {
		this.workType = workType;
	}

	public int getOpenings() {
		return openings;
	}

	public void setOpenings(int openings) {
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

	public JobStatus getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(JobStatus jobStatus) {
		this.jobStatus = jobStatus;
	}

	public ApplicationStatus getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(ApplicationStatus approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public Set<Skills> getRequiredSkills() {
		return requiredSkills;
	}

	public void setRequiredSkills(Set<Skills> requiredSkills) {
		this.requiredSkills = requiredSkills;
	}

	public Set<Location> getJobLocation() {
		return jobLocation;
	}

	public void setJobLocation(Set<Location> jobLocation) {
		this.jobLocation = jobLocation;
	}
	
}
