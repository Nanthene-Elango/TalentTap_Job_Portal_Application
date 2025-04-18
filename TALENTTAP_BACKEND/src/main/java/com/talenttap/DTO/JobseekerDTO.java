package com.talenttap.DTO;

import java.time.LocalDate;

import com.talenttap.entity.JobSeeker;
import com.talenttap.entity.Users;

import lombok.Data;

@Data
public class JobseekerDTO {

	private int id;
	private LocalDate dob;
	private String gender;
	private String summary;
	private int yearOfExperience;
	private String location;
	private String phone;
	private String fullName;
	private String password;
	private String username;
	private String email;
	
	public JobseekerDTO(JobSeeker jobseeker , Users user) {
	
		this.id = jobseeker.getJobSeekerId();
		this.dob = jobseeker.getDob();
		this.gender = jobseeker.getGender().name();
		this.summary = jobseeker.getProfileSummary();
		this.yearOfExperience = jobseeker.getYearOfExperience();
		this.location = jobseeker.getLocation().getLocation();
		this.phone = user.getMobileNumber();
		this.fullName = user.getFullName();
		this.password = user.getPassword();
		this.username = user.getUsername();
		this.email = user.getEmail();
	}
}