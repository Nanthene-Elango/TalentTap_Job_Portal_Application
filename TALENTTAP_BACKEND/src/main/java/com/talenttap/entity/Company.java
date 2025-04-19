package com.talenttap.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
public class Company {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="company_id")
	private int CompanyId;
	
	@Column(name="company_name" , nullable=false , unique = true)
	private String companyName;
	
	@Lob
	@Column(name = "company_logo", columnDefinition = "LONGBLOB" ,  nullable=false)
	private byte[] companyLogo;
	
	@ManyToOne
	@JoinColumn(name="industry_type" , nullable=false)
	private IndustryType industryType;
	
	@Column(name="company_email" , nullable=false , unique=true)
	private String email;
	
	@Column(name="company_phone" , nullable=false , unique=true)
	private String phoneNumber;
	
	@Column(name="company_size" , nullable=false)
	private String companySize;
	
	@ManyToOne
	@JoinColumn(name="company_location" , nullable=false)
	private Location location;
	
	@Column(name="website_url")
	private String websiteUrl;
	
	@Column(name="founded_at" , nullable=false)
	private int foundedAt;
	
	@Column(name="about" , nullable=false , columnDefinition="TEXT")
	private String about;
	
	@Column(name="is_verified")
	private boolean isVerified;

	public int getCompanyId() {
		return CompanyId;
	}

	public void setCompanyId(int companyId) {
		CompanyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public byte[] getCompanyLogo() {
		return companyLogo;
	}

	public void setCompanyLogo(byte[] companyLogo) {
		this.companyLogo = companyLogo;
	}

	public IndustryType getIndustryType() {
		return industryType;
	}

	public void setIndustryType(IndustryType industryType) {
		this.industryType = industryType;
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

	public String getCompanySize() {
		return companySize;
	}

	public void setCompanySize(String companySize) {
		this.companySize = companySize;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

	public int getFoundedAt() {
		return foundedAt;
	}

	public void setFoundedAt(int foundedAt) {
		this.foundedAt = foundedAt;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public boolean isVerified() {
		return isVerified;
	}

	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}
	
}
