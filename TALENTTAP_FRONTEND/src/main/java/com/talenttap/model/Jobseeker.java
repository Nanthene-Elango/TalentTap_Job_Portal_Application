package com.talenttap.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Jobseeker {

	private int id;
	private LocalDate dob;
	private String gender;
	private String summary;
	private int yearOfExperience;
	private String location;
	private String phoneNumber;
	private String fullName;
	private String password;
	private String username;
	private String email;
}
