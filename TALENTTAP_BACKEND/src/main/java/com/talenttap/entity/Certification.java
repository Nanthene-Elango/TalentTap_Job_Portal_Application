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
public class Certification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "certification_id")
	private int certification_id;
	
	@ManyToOne
	@JoinColumn(name = "jobseeker_id", nullable = false)
	private JobSeeker jobSeeker;
	
	@Column(name = "certification_number")
	private String certificationNumber;
	
	@Column(name = "certification_name")
	private String certificationName;
	
	@Column(name = "certificate_url")
	private String certificateURL;
	
	@Column(name = "issue_date" , nullable = false)
	private LocalDate issueDate;
	
	@Column(name = "expiry_date")
	private LocalDate expiryDate;
	
}
