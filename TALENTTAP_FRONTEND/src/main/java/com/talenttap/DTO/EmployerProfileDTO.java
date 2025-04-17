package com.talenttap.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployerProfileDTO {
	private String fullname;
	private String username;
	private String phoneNumber;
	private String email;
	private String companyLogo;
	private String companyName;
	private String industryType;
	private String companyEmail;
	private String companyPhone;
	private String designation;
	private String about;
	private String companySize;
	private String location;
}

