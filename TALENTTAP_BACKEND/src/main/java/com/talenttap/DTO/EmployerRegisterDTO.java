// com.talenttap.DTO.EmployerRegisterDTO
package com.talenttap.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

public class EmployerRegisterDTO {
    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Username is required")
    @Size(min = 3, message = "Username must be at least 3 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String emailId;

    @NotBlank(message = "Company name is required")
    private String companyName;

    private MultipartFile companyLogo; // Optional

    @NotNull(message = "Industry type is required")
    private Integer industryType;

    @NotBlank(message = "Company email is required")
    @Email(message = "Invalid company email format")
    private String companyEmail;

    @NotBlank(message = "Company phone number is required")
    @Pattern(regexp = "\\d{10}", message = "Company phone number must be 10 digits")
    private String companyPhoneNumber;

    @NotBlank(message = "Company size is required")
    private String companySize;

    @NotNull(message = "Location is required")
    private Integer locationId;

    @NotBlank(message = "Website URL is required")
    @URL(message = "Invalid URL format")
    private String webUrl;

    @NotNull(message = "Founded year is required")
    @Min(value = 1800, message = "Founded year must be after 1800")
    @Max(value = 2025, message = "Founded year cannot be in the future")
    private Integer foundedAt;

    @NotBlank(message = "Company description is required")
    private String about;

    @NotBlank(message = "Designation is required")
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