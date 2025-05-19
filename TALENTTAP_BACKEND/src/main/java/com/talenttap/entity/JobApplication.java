package com.talenttap.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
public class JobApplication {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "job_application_id")
	private int jobApplicationId;
	
	@ManyToOne
	@JoinColumn(name = "job_id" , nullable = false)
	private Jobs job;
	
	@ManyToOne
	@JoinColumn(name = "jobseeker_id" , nullable = false)
	private JobSeeker jobSeeker;
	
	@Column(name = "date_of_application" , nullable = false)
	private LocalDateTime dateOfApplication;
	
	@Column(name = "last_updated")
	private LocalDateTime lastUpdated;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private ApplicationStatus status = ApplicationStatus.pending;

	public int getJobApplicationId() {
		return jobApplicationId;
	}

	public void setJobApplicationId(int jobApplicationId) {
		this.jobApplicationId = jobApplicationId;
	}

	public Jobs getJob() {
		return job;
	}

	public void setJob(Jobs job) {
		this.job = job;
	}

	public JobSeeker getJobSeeker() {
		return jobSeeker;
	}

	public void setJobSeeker(JobSeeker jobSeeker) {
		this.jobSeeker = jobSeeker;
	}

	public LocalDateTime getDateOfApplication() {
		return dateOfApplication;
	}

	public void setDateOfApplication(LocalDateTime dateOfApplication) {
		this.dateOfApplication = dateOfApplication;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public ApplicationStatus getStatus() {
		return status;
	}

	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}

}
