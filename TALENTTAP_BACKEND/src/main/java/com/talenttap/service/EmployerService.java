package com.talenttap.service;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.talenttap.DTO.CompanyUpdateDTO;
import com.talenttap.DTO.LoginDTO;
import com.talenttap.entity.ApplicationStatus;
import com.talenttap.entity.Company;
import com.talenttap.entity.Employer;
import com.talenttap.entity.IndustryType;
import com.talenttap.entity.JobApplication;
import com.talenttap.entity.Users;
import com.talenttap.exceptions.InvalidCredentialsException;
import com.talenttap.repository.CompanyRepository;
import com.talenttap.repository.EmployerRepository;
import com.talenttap.repository.IndustryTypeRepository;
import com.talenttap.repository.JobApplicationRepository;
import com.talenttap.repository.LocationRepository;
import com.talenttap.repository.UsersRepository;
import com.talenttap.security.JwtUtil;

import jakarta.servlet.http.Cookie;

@Service
public class EmployerService {
	
	private EmployerRepository employerRepo;
	
	private JwtUtil jwtUtil;
	
	private UsersRepository userRepo;
	
	private LocationRepository locationRepo;
	
	private IndustryTypeRepository industryTypeRepo;
	
	private CompanyRepository companyRepo;
	
	private PasswordEncoder passwordEncoder;
	
	private JobApplicationRepository jobApplicationRepository;
	
	
	public EmployerService(EmployerRepository employerRepo, JwtUtil jwtUtil, 
			UsersRepository userRepo,LocationRepository locationRepo, 
			IndustryTypeRepository industryTypeRepo, CompanyRepository companyRepo, PasswordEncoder passwordEncoder, JobApplicationRepository jobApplicationRepository) {
		this.employerRepo = employerRepo;
		this.jwtUtil = jwtUtil;
		this.userRepo = userRepo;
		this.locationRepo = locationRepo;
		this.industryTypeRepo = industryTypeRepo;
		this.companyRepo = companyRepo;
		this.passwordEncoder =  passwordEncoder;
		this.jobApplicationRepository = jobApplicationRepository;
	}
	
	// update profile photo
	public ResponseEntity<String> updateCompanyProfilePhoto(MultipartFile file, Integer employerId) {

		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body("No file uploaded");
		}

		try {
			Employer employer = employerRepo.findById(employerId).orElse(null);
			if (employer != null) {
				employer.getCompany().setCompanyLogo(file.getBytes());
				employerRepo.save(employer);
				return ResponseEntity.ok("Profile photo updated");
			} else {
				return ResponseEntity.badRequest().body("Employer not found");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading photo");
		}
	}

	public ResponseEntity<String> updateCompanyProfile(CompanyUpdateDTO dto, String jwt) throws Exception {
		 // Validate JWT
        if (jwt == null || jwt.isBlank()) {
            throw new IllegalArgumentException("JWT token is empty or null");
        }

        // Extract username from JWT
        String username = jwtUtil.extractIdentifier(jwt);
        Users user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

	    Employer employer = employerRepo.findByUser(user).get();
	    
	     Company company = employer.getCompany();
	     
	     company.setCompanyName(dto.getCompanyName());
	     
	     company.setIndustryType(industryTypeRepo.findById(dto.getIndustryType())
	                .orElseThrow(() -> new Exception("Industry type not found")));
	     
	     company.setLocation(locationRepo.findById(dto.getLocation())
	                .orElseThrow(() -> new Exception("Location not found")));
	     
	     company.setFoundedAt(dto.getFoundedAt());
	    
	     
	     company.setPhoneNumber(dto.getPhoneNumber());
	     company.setWebsiteUrl(dto.getWebsiteUrl());
	     company.setAbout(dto.getAbout());
	     company.setEmail(dto.getEmail());
	     company.setCompanySize(dto.getCompanySize());
	     
	     companyRepo.save(company);
	     
		return null;
	    
	}
	
	// verify employer
	public String verifyEmployer(LoginDTO dto, String jwt) {
		 if (jwt == null || jwt.isBlank()) {
	            throw new IllegalArgumentException("JWT token is empty or null");
	        }

	        // Extract username from JWT
	        String username = jwtUtil.extractIdentifier(jwt);
	        Users user = userRepo.findByUsername(username)
	                .orElseThrow(() -> new RuntimeException("User not found: " + username));
	   

			if (user != null && passwordEncoder.matches(dto.getPassword(), user.getPassword())) {

				return "User verification successfull";
			} else {
				throw new InvalidCredentialsException("Invalid Username/Password!");
			}
		
	}

	public JobApplication approveApplication(int id, String token) {
		JobApplication application = jobApplicationRepository.findById(id).get();
		application.setStatus(ApplicationStatus.approved);
		application.setLastUpdated(LocalDateTime.now());
		JobApplication updatedApplication = jobApplicationRepository.save(application);
		return updatedApplication;
	}
	
	public JobApplication rejectApplication(int id, String token) {
		JobApplication application = jobApplicationRepository.findById(id).get();
		application.setStatus(ApplicationStatus.rejected);
		application.setLastUpdated(LocalDateTime.now());
		JobApplication updatedApplication = jobApplicationRepository.save(application);
		return updatedApplication;
	}
	

}
