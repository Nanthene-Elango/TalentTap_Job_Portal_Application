package com.talenttap.DTO;

import java.time.LocalDate;

import com.talenttap.entity.JobSeeker;
import com.talenttap.entity.Users;

import lombok.Data;

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
	private byte[] resume;
	
	
	public JobseekerDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JobseekerDTO(JobSeeker jobseeker , Users user) {
	
		this.id = jobseeker.getJobSeekerId();
		this.dob = jobseeker.getDob();
		this.gender = jobseeker.getGender() != null ? jobseeker.getGender().name() : null;
		this.summary = jobseeker.getProfileSummary();
		this.yearOfExperience = jobseeker.getYearOfExperience();
		this.location = jobseeker.getLocation().getLocation();
		this.phone = user.getMobileNumber();
		this.fullName = user.getFullName();
		this.password = user.getPassword();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.resume = jobseeker.getResume();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public int getYearOfExperience() {
		return yearOfExperience;
	}

	public void setYearOfExperience(int yearOfExperience) {
		this.yearOfExperience = yearOfExperience;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte[] getResume() {
		return resume;
	}

	public void setResume(byte[] resume) {
		this.resume = resume;
	}

}