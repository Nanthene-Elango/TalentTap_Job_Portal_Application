// com.talenttap.DTO.EmployerRegisterDTO
package com.talenttap.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

public class EmployerRegisterDTO {
    private String fullName;

    private String username;

    private String password;
    
    private String phoneNumber;

    private String emailId;

    private String companyName;

    private MultipartFile companyLogo;

    private Integer industryType;

    private String companyEmail;

    private String companyPhoneNumber;

    private String companySize;

    private Integer locationId;

    private String webUrl;
    
    private Integer foundedAt;

    private String about;

    private String designation;

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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public MultipartFile getCompanyLogo() {
		return companyLogo;
	}

	public void setCompanyLogo(MultipartFile companyLogo) {
		this.companyLogo = companyLogo;
	}

	public Integer getIndustryType() {
		return industryType;
	}

	public void setIndustryType(Integer industryType) {
		this.industryType = industryType;
	}

	public String getCompanyEmail() {
		return companyEmail;
	}

	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	public String getCompanyPhoneNumber() {
		return companyPhoneNumber;
	}

	public void setCompanyPhoneNumber(String companyPhoneNumber) {
		this.companyPhoneNumber = companyPhoneNumber;
	}

	public String getCompanySize() {
		return companySize;
	}

	public void setCompanySize(String companySize) {
		this.companySize = companySize;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	public Integer getFoundedAt() {
		return foundedAt;
	}

	public void setFoundedAt(Integer foundedAt) {
		this.foundedAt = foundedAt;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}
    
    
}