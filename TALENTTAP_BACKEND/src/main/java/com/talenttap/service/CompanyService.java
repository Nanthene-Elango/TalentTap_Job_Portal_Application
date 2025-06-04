package com.talenttap.service;

import java.util.Base64;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.talenttap.DTO.CompanyProfileDTO;
import com.talenttap.entity.Company;
import com.talenttap.entity.Employer;
import com.talenttap.entity.IndustryType;
import com.talenttap.entity.Location;
import com.talenttap.entity.Users;
import com.talenttap.exceptions.BadCredentialException;
import com.talenttap.exceptions.CompanyNotFoundException;
import com.talenttap.exceptions.EmployerNotFoundException;
import com.talenttap.mapper.CompanyMapper;
import com.talenttap.repository.CompanyRepository;
import com.talenttap.repository.EmployerRepository;
import com.talenttap.repository.IndustryTypeRepository;
import com.talenttap.repository.LocationRepository;
import com.talenttap.repository.UsersRepository;
import com.talenttap.security.JwtUtil;

@Service
public class CompanyService {
	private EmployerRepository employerRepo;

	private JwtUtil jwtUtil;

	private UsersRepository userRepo;

	private LocationRepository locationRepo;

	private IndustryTypeRepository industryTypeRepo;

	private CompanyRepository companyRepo;

	private CompanyMapper companyMapper;

	public CompanyService(EmployerRepository employerRepo, JwtUtil jwtUtil, UsersRepository userRepo,
			LocationRepository locationRepo, IndustryTypeRepository industryTypeRepo, CompanyRepository companyRepo,
			PasswordEncoder passwordEncoder, CompanyMapper companyMapper) {
		this.employerRepo = employerRepo;
		this.jwtUtil = jwtUtil;
		this.userRepo = userRepo;
		this.locationRepo = locationRepo;
		this.industryTypeRepo = industryTypeRepo;
		this.companyRepo = companyRepo;
		this.companyMapper = companyMapper;
	}

	public CompanyProfileDTO getCompanyProfile(String jwt) {
		// Extract the token
		jwt = jwt.replace("Bearer ", "");

		// Validate JWT
		if (jwt == null || jwt.isBlank()) {
			throw new IllegalArgumentException("JWT token must not be null or empty");
		}

		// Extract username from JWT
		String username = jwtUtil.extractIdentifier(jwt);
		if (username == null || username.isBlank()) {
			throw new IllegalArgumentException("Invalid JWT: username missing");
		}

		// Fetch user
		Users user = userRepo.findByUsername(username)
				.orElseThrow(() -> new BadCredentialException("User not found: " + username));

		// Fetch employer
		Employer employer = employerRepo.findByUser(user)
				.orElseThrow(() -> new EmployerNotFoundException("Employer not found for user: " + username));

		// Fetch company
		Company company = employer.getCompany();
		if (company == null) {
			throw new CompanyNotFoundException("No company associated with employer: " + employer.getEmployerId());
		}

		return companyMapper.toCompanyProfileDTO(company);
	}

	public String updateCompanyProfile(String jwt, CompanyProfileDTO profile) {
		// Extract the token
		jwt = jwt.replace("Bearer ", "");

		// Validate JWT
		if (jwt == null || jwt.isBlank()) {
			throw new IllegalArgumentException("JWT token must not be null or empty");
		}

		// Extract username from JWT
		String username = jwtUtil.extractIdentifier(jwt);
		if (username == null || username.isBlank()) {
			throw new IllegalArgumentException("Invalid JWT: username missing");
		}

		// Fetch user
		Users user = userRepo.findByUsername(username)
				.orElseThrow(() -> new BadCredentialException("User not found: " + username));

		// Fetch employer
		Employer employer = employerRepo.findByUser(user)
				.orElseThrow(() -> new EmployerNotFoundException("Employer not found for user: " + username));

		// Fetch company
		Company company = employer.getCompany();
		if (company == null) {
			throw new CompanyNotFoundException("No company associated with employer: " + employer.getEmployerId());
		}

		company.setCompanyName(profile.getCompanyName());
		company.setEmail(profile.getEmail());
		company.setPhoneNumber(profile.getPhoneNumber());
		company.setCompanySize(profile.getCompanySize());
		company.setWebsiteUrl(profile.getWebsiteUrl());
		company.setFoundedAt(profile.getFoundedAt());
		company.setAbout(profile.getAbout());

		// Fetch and set industryType
		System.out.println(profile.getIndustryType().getIndustryType());
		System.out.println(profile.getIndustryType().getIndustryTypeId());

		if (profile.getIndustryType() != null) {
			IndustryType industry = industryTypeRepo.findById(profile.getIndustryType().getIndustryTypeId())
					.orElseThrow(() -> new RuntimeException("Industry not found"));
			company.setIndustryType(industry);
		}

		// Fetch and set location
		if (profile.getLocation() != null) {
			Location location = locationRepo.findById(profile.getLocation().getLocationId())
					.orElseThrow(() -> new RuntimeException("Location not found"));
			company.setLocation(location);
		}

		companyRepo.save(company);

		return "Company updated successfully";
	}

	public String getCompanyLogo(String jwt) {
		jwt = jwt.replace("Bearer ", "");

		// Validate JWT
		if (jwt == null || jwt.isBlank()) {
			throw new IllegalArgumentException("JWT token must not be null or empty");
		}

		// Extract username from JWT
		String username = jwtUtil.extractIdentifier(jwt);
		if (username == null || username.isBlank()) {
			throw new IllegalArgumentException("Invalid JWT: username missing");
		}

		// Fetch user
		Users user = userRepo.findByUsername(username)
				.orElseThrow(() -> new BadCredentialException("User not found: " + username));

		// Fetch employer
		Employer employer = employerRepo.findByUser(user)
				.orElseThrow(() -> new EmployerNotFoundException("Employer not found for user: " + username));

		// Fetch company
		Company company = employer.getCompany();
		if (company == null) {
			throw new CompanyNotFoundException("No company associated with employer: " + employer.getEmployerId());
		}

		// Convert logo to base64
		String companyLogoBase64 = company.getCompanyLogo() != null && company.getCompanyLogo().length > 0
				? Base64.getEncoder().encodeToString(company.getCompanyLogo())
				: null;

		return companyLogoBase64;

	}

	public String updateCompanyLogo(String jwt, byte[] logo) {
		jwt = jwt.replace("Bearer ", "");

		// Validate JWT
		if (jwt == null || jwt.isBlank()) {
			throw new IllegalArgumentException("JWT token must not be null or empty");
		}

		// Extract username from JWT
		String username = jwtUtil.extractIdentifier(jwt);
		if (username == null || username.isBlank()) {
			throw new IllegalArgumentException("Invalid JWT: username missing");
		}

		// Fetch user
		Users user = userRepo.findByUsername(username)
				.orElseThrow(() -> new BadCredentialException("User not found: " + username));

		// Fetch employer
		Employer employer = employerRepo.findByUser(user)
				.orElseThrow(() -> new EmployerNotFoundException("Employer not found for user: " + username));

		// Fetch company
		Company company = employer.getCompany();
		if (company == null) {
			throw new CompanyNotFoundException("No company associated with employer: " + employer.getEmployerId());
		}

		company.setCompanyLogo(logo);
		companyRepo.save(company);
		return "Logo updated successfully";
	}

}
