package com.talenttap.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.talenttap.DTO.CompanyUpdateDTO;
import com.talenttap.DTO.DashboardMetrics;
import com.talenttap.DTO.LoginDTO;
import com.talenttap.entity.ApplicationStatus;
import com.talenttap.entity.Company;
import com.talenttap.entity.Employer;
import com.talenttap.entity.IndustryType;
import com.talenttap.entity.JobApplication;
import com.talenttap.entity.JobStatus;
import com.talenttap.entity.Jobs;
import com.talenttap.entity.Users;
import com.talenttap.exceptions.InvalidCredentialsException;
import com.talenttap.repository.CompanyRepository;
import com.talenttap.repository.EmployerRepository;
import com.talenttap.repository.IndustryTypeRepository;
import com.talenttap.repository.JobApplicationRepository;
import com.talenttap.repository.JobsRepository;
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

	private JobsRepository jobsRepo;

	public EmployerService(EmployerRepository employerRepo, JwtUtil jwtUtil, UsersRepository userRepo,
			LocationRepository locationRepo, IndustryTypeRepository industryTypeRepo, CompanyRepository companyRepo,
			PasswordEncoder passwordEncoder, JobApplicationRepository jobApplicationRepository,
			JobsRepository jobsRepo) {
		this.employerRepo = employerRepo;
		this.jwtUtil = jwtUtil;
		this.userRepo = userRepo;
		this.locationRepo = locationRepo;
		this.industryTypeRepo = industryTypeRepo;
		this.companyRepo = companyRepo;
		this.passwordEncoder = passwordEncoder;
		this.jobApplicationRepository = jobApplicationRepository;
		this.jobsRepo = jobsRepo;
	}

	// verify employer
	public String verifyEmployer(String password, String jwt) {
		if (jwt == null || jwt.isBlank()) {
			throw new IllegalArgumentException("JWT token is empty or null");
		}

		// Extract username from JWT
		String username = jwtUtil.extractIdentifier(jwt);
		Users user = userRepo.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found: " + username));
		if (user != null) {
			user.setPassword(password);
			return "Password updated successfull";
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

	public DashboardMetrics getAllDashboardMetrics(String jwt) {
		DashboardMetrics dm = new DashboardMetrics();

		if (jwt == null || jwt.isBlank()) {
			throw new IllegalArgumentException("JWT token is empty or null");
		}

		// Extract username from JWT
		String username = jwtUtil.extractIdentifier(jwt);
		Users user = userRepo.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found: " + username));

		Employer employer = employerRepo.findByUser(user).get();

		JobStatus jobStatus = JobStatus.open;
		List<Jobs> openJobs = jobsRepo.findByEmployerAndJobStatus(employer, jobStatus);

		JobStatus jobStatusClosed = JobStatus.closed;
		List<Jobs> closedJobs = jobsRepo.findByEmployerAndJobStatus(employer, jobStatusClosed);

		JobStatus jobStatusExpired = JobStatus.expired;
		List<Jobs> expiredJobs = jobsRepo.findByEmployerAndJobStatus(employer, jobStatusExpired);

		List<Jobs> jobs = jobsRepo.findByEmployer(employer);

		List<JobApplication> jobApplication = jobApplicationRepository.findByJob_Employer(employer);
		
		List<JobApplication> accepted = jobApplicationRepository.findByJob_EmployerAndStatus(employer, ApplicationStatus.approved);
		
		List<JobApplication> rejected = jobApplicationRepository.findByJob_EmployerAndStatus(employer, ApplicationStatus.rejected);
		List<JobApplication> pending = jobApplicationRepository.findByJob_EmployerAndStatus(employer, ApplicationStatus.pending);

		dm.setActiveJobs(openJobs.size());
		dm.setCloseJobs(closedJobs.size());
		dm.setExpiredJobs(expiredJobs.size());
		dm.setTotalJobs(jobs.size());
		dm.setTotalApplications(jobApplication.size());
		dm.setApprovedApplications(accepted.size());
		dm.setRejectedApplications(rejected.size());
		dm.setPendingApplication(pending.size());

		System.out.println(openJobs.size());
//	        
		return dm;

	}

	public boolean verifyEmployerAndCompany(String jwt) {
		if (jwt == null || jwt.isBlank()) {
			throw new IllegalArgumentException("JWT token is empty or null");
		}

		// Extract username from JWT
		String username = jwtUtil.extractIdentifier(jwt);
		Users user = userRepo.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found: " + username));

		Employer employer = employerRepo.findByUser(user).get();

		Company company = employer.getCompany();

		company.setVerified(true);
		companyRepo.save(company);
		return true;
	}

	public Boolean isCompanyVerified(String jwt) {
		if (jwt == null || jwt.isBlank()) {
			throw new IllegalArgumentException("JWT token is empty or null");
		}

		// Extract username from JWT
		String username = jwtUtil.extractIdentifier(jwt);
		Users user = userRepo.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found: " + username));

		Employer employer = employerRepo.findByUser(user).get();

		Company company = employer.getCompany();

		if (company.isVerified()) {
			return true;
		} else {
			return false;
		}
	}

}
