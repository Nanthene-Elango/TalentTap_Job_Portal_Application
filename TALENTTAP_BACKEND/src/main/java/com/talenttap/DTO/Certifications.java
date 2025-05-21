package com.talenttap.DTO;

import java.time.LocalDate;

import com.talenttap.entity.Certification;

public class Certifications {

	private Integer id;
	private String title;
	private String issuedBy;
	private LocalDate issued_date;
	private LocalDate expiry_date;
	private String url;
	private String number;
	
	public Certifications() {
		super();
	}
	
	public Certifications(Certification certification) {
		super();
		this.id = certification.getCertification_id();
		this.title = certification.getCertificationName();
		this.issuedBy = certification.getIssuedBy();
		this.issued_date = certification.getIssueDate();
		this.expiry_date = certification.getExpiryDate();
		this.url = certification.getCertificateURL();
		this.number = certification.getCertificationNumber();
	}
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIssuedBy() {
		return issuedBy;
	}
	public void setIssuedBy(String issuedBy) {
		this.issuedBy = issuedBy;
	}
	public LocalDate getIssued_date() {
		return issued_date;
	}
	public void setIssued_date(LocalDate issued_date) {
		this.issued_date = issued_date;
	}
	public LocalDate getExpiry_date() {
		return expiry_date;
	}
	public void setExpiry_date(LocalDate expiry_date) {
		this.expiry_date = expiry_date;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
}
