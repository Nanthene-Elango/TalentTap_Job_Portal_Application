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
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	
	@Column(name = "username" , unique = true)
	private String username;
	
	@Column(name = "password" , nullable = false)
	private String password;
	
	@Column(name = "email" , nullable = false , unique = true)
	private String email;
	
	@Column(name = "mobile_number" , unique = true , length = 10)
	private String mobileNumber;
	
	@Column(name = "full_name" , nullable = false)
	private String fullName;
	
	@ManyToOne
	@JoinColumn(name = "role_id", nullable = false)
	private Roles role;
	
	@Column(name = "google_id" , unique = true)
	private String googleId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "auth_provider")
	private AuthProvider authProvider;
	
	@Column(name = "date_of_registration" , nullable = false)
	private LocalDate dateOfRegistration;
}

