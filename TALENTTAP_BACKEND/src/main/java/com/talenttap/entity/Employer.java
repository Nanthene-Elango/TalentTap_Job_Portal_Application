package com.talenttap.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Employer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "employer_id")
	private int employerId;
	
	@OneToOne
	@JoinColumn(name="user_id" , nullable = false, unique = true)
	private Users user;
	
	@OneToOne
	@JoinColumn(name="company_id" , nullable=false , unique=true)
	private Company company;
	
	@Column(name="designation" , nullable = false)
	private String designation;
	
}
