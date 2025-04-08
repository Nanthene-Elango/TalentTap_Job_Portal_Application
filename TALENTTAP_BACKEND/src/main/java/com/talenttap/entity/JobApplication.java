package com.talenttap.entity;

import java.time.LocalDate;

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

@Data
@AllArgsConstructor
@NoArgsConstructor
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
	private LocalDate dateOfApplication;
	
	@Column(name = "last_updated")
	private LocalDate lastUpdated;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private ApplicationStatus status = ApplicationStatus.pending;

}
