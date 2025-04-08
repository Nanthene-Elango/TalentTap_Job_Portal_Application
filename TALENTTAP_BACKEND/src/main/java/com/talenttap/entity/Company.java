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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Company {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="company_id")
	private int CompanyId;
	
	@Column(name="company_name" , nullable=false , unique = true)
	private String companyName;
	
	@Lob
	@Column(name="company_logo" , nullable=false)
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
	
}
