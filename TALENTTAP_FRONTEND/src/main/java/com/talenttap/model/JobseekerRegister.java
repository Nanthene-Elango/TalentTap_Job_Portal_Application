package com.talenttap.model;

import java.util.List;

public class JobseekerRegister {

    private Integer yearsOfExperience;
    private Integer location;
    private Integer highestQualification;
    private String degree;
    private String institution;
    private Integer startYear;
    private Integer endYear;
    private String boardOfStudy;
    private Float percentage;
    private String fullName;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private List<Integer> skillIds;
    
	public JobseekerRegister() {
		super();
		// TODO Auto-generated constructor stub
	}
	public JobseekerRegister(Integer yearsOfExperience, Integer location, Integer highestQualification, String degree,
			String institution, Integer startYear, Integer endYear, String boardOfStudy, Float percentage,
			String fullName, String username, String password, String email, String phoneNumber,
			List<Integer> skillIds) {
		super();
		this.yearsOfExperience = yearsOfExperience;
		this.location = location;
		this.highestQualification = highestQualification;
		this.degree = degree;
		this.institution = institution;
		this.startYear = startYear;
		this.endYear = endYear;
		this.boardOfStudy = boardOfStudy;
		this.percentage = percentage;
		this.fullName = fullName;
		this.username = username;
		this.password = password;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.skillIds = skillIds;
	}
	public Integer getYearsOfExperience() {
		return yearsOfExperience;
	}
	public void setYearsOfExperience(Integer yearsOfExperience) {
		this.yearsOfExperience = yearsOfExperience;
	}
	public Integer getLocation() {
		return location;
	}
	public void setLocation(Integer location) {
		this.location = location;
	}
	public Integer getHighestQualification() {
		return highestQualification;
	}
	public void setHighestQualification(Integer highestQualification) {
		this.highestQualification = highestQualification;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getInstitution() {
		return institution;
	}
	public void setInstitution(String institution) {
		this.institution = institution;
	}
	public Integer getStartYear() {
		return startYear;
	}
	public void setStartYear(Integer startYear) {
		this.startYear = startYear;
	}
	public Integer getEndYear() {
		return endYear;
	}
	public void setEndYear(Integer endYear) {
		this.endYear = endYear;
	}
	public String getBoardOfStudy() {
		return boardOfStudy;
	}
	public void setBoardOfStudy(String boardOfStudy) {
		this.boardOfStudy = boardOfStudy;
	}
	public Float getPercentage() {
		return percentage;
	}
	public void setPercentage(Float percentage) {
		this.percentage = percentage;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public List<Integer> getSkillIds() {
		return skillIds;
	}
	public void setSkillIds(List<Integer> skillIds) {
		this.skillIds = skillIds;
	}
}
