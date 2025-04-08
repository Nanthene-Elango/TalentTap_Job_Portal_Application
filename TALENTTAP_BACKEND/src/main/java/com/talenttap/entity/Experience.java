package com.talenttap.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Experience {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "experience_id")
	private int experienceId;
	
	@ManyToOne
	@JoinColumn(name = "jobseeker_id", nullable = false)
	private JobSeeker jobSeeker;
	
	@Column(name = "description" , columnDefinition = "TEXT")
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "employment_type_id" , nullable = false)
	private EmploymentType employmentType;
	
	@Column(name = "company_name" , nullable = false)
	private String companyName;
	
	@Column(name = "start_date" , nullable = false)
	private LocalDate startDate;
	
	@Column(name = "end_date")
	private LocalDate endDate;
	
}
