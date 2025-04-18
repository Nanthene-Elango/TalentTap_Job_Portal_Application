package com.talenttap.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.talenttap.DTO.EmploymentTypeDTO;
import com.talenttap.DTO.JobCategoryDTO;
import com.talenttap.DTO.JobDisplayDTO;
import com.talenttap.DTO.JobFormDTO;
import com.talenttap.DTO.LocationDTO;
import com.talenttap.entity.ApplicationStatus;
import com.talenttap.entity.Company;
import com.talenttap.entity.Employer;
import com.talenttap.entity.EmploymentType;
import com.talenttap.entity.JobCategory;
import com.talenttap.entity.JobStatus;
import com.talenttap.entity.Jobs;
import com.talenttap.entity.SalaryRange;
import com.talenttap.entity.Users;
import com.talenttap.entity.WorkType;
import com.talenttap.repository.EmployerRepository;
import com.talenttap.repository.EmploymentTypeRepository;
import com.talenttap.repository.JobCategoryRepository;
import com.talenttap.repository.LocationRepository;
import com.talenttap.repository.SalaryRangeRepository;
import com.talenttap.repository.SkillsRepository;
import com.talenttap.repository.UsersRepository;
import com.talenttap.repository.JobsRepository;
import com.talenttap.security.JwtUtil;

@Service
public class JobService {
	private EmploymentTypeRepository employmentTypeRepo;
	
	private JobCategoryRepository jobCategoryRepository;
	
	private JwtUtil jwtutil;
	
	private UsersRepository userRepo;
	
	private EmployerRepository employerRepo;
	
	private SkillsRepository skillsRepo;
	
	private LocationRepository locationRepo;
	
	private JobsRepository jobsRepo;
	
	private SalaryRangeRepository salaryRangeRepo;
	
	public JobService(EmploymentTypeRepository employmentTypeRepo, JobCategoryRepository jobCategoryRepository, 
			JwtUtil jwtutil,UsersRepository userRepo, EmployerRepository employerRepo,
			LocationRepository locationRepo, SkillsRepository skillsRepo, JobsRepository jobsRepo, SalaryRangeRepository salaryRangeRepo) {
		this.employmentTypeRepo = employmentTypeRepo;
		this.jobCategoryRepository = jobCategoryRepository;
		this.jwtutil = jwtutil;
		this.userRepo = userRepo;
		this.employerRepo = employerRepo;
		this.locationRepo = locationRepo;
		this.skillsRepo = skillsRepo;
		this.jobsRepo = jobsRepo;
		this.salaryRangeRepo = salaryRangeRepo;
	}
	
	public List<EmploymentTypeDTO> getAllEmploymentType(){
		List<EmploymentTypeDTO> list =employmentTypeRepo.findAll().stream().map(EmploymentTypeDTO::new).toList();
		return list;
	}
	 
	public List<JobCategoryDTO> getAllJobCategory(){
		List<JobCategoryDTO> jobCategory =  jobCategoryRepository.findAll().stream().map(JobCategoryDTO::new).toList();
		return jobCategory;
	}

	public String saveJob(JobFormDTO jobFormDTO, String jwt) {
	    // Log JWT for debugging
	    System.out.println("Received JWT: " + jwt);

	    // Extract username from JWT
	    String username = jwtutil.extractIdentifier(jwt);
	    System.out.println("Extracted username: " + username);

	    // Fetch user
	    Users user = userRepo.findByUsername(username)
	            .orElseThrow(() -> new RuntimeException("User not found: " + username));

	    // Fetch employer
	    Employer employer = employerRepo.findByUser(user)
	            .orElseThrow(() -> new RuntimeException("Employer not found for user: " + username));

	    // Fetch job category and employment type
	    JobCategory jobCategory = jobCategoryRepository.findById(jobFormDTO.getJobCategoryId())
	            .orElseThrow(() -> new RuntimeException("Job category not found: " + jobFormDTO.getJobCategoryId()));
	    EmploymentType employmentType = employmentTypeRepo.findById(jobFormDTO.getJobTypeId())
	            .orElseThrow(() -> new RuntimeException("Employment type not found: " + jobFormDTO.getJobTypeId()));

	    // Create Jobs entity
	    Jobs job = new Jobs();
	    job.setEmployer(employer);
	    job.setJobRole(jobFormDTO.getJobRole());
	    job.setJobCategory(jobCategory);
	    job.setJobType(employmentType);
	    job.setJobDescription(jobFormDTO.getJobDescription());

	    // Add skills
	    for (Integer skillId : jobFormDTO.getSkillIds()) {
	        job.getRequiredSkills().add(skillsRepo.findById(skillId)
	                .orElseThrow(() -> new RuntimeException("Skill not found: " + skillId)));
	    }

	    // Add locations
	    for (Integer locationId : jobFormDTO.getLocationIds()) {
	        job.getJobLocation().add(locationRepo.findById(locationId)
	                .orElseThrow(() -> new RuntimeException("Location not found: " + locationId)));
	    }

	    // Handle SalaryRange
	    SalaryRange salary = new SalaryRange();
	    salary.setMax_range(jobFormDTO.getSalaryMax() != null ? jobFormDTO.getSalaryMax() : 0.0);
	    salary.setMin_range(jobFormDTO.getSalaryMin() != null ? jobFormDTO.getSalaryMin() : 0.0);
	    // Save and flush SalaryRange to ensure it's persisted
	    salary = salaryRangeRepo.saveAndFlush(salary);
	    job.setSalary_range(salary);

	    // Set other job fields
	    job.setRoles(jobFormDTO.getRequirements());
	    job.setResponsibilities(jobFormDTO.getResponsibilities());
	    job.setBenefits(jobFormDTO.getBenefits());
	    job.setOpenings(jobFormDTO.getOpenings());
	    job.setStipend(jobFormDTO.getStipend() != null ? jobFormDTO.getStipend() : 0.0);
	    job.setDuration(jobFormDTO.getDuration() != null ? jobFormDTO.getDuration() : 0);
	    job.setYearsOfExperience(jobFormDTO.getYearsOfExperience());
	    job.setPostedDate(LocalDateTime.now());

	    // Convert workType string to WorkType enum
	    WorkType workType;
	    try {
	        workType = WorkType.valueOf(jobFormDTO.getWorkType().toUpperCase());
	    } catch (IllegalArgumentException | NullPointerException e) {
	        throw new IllegalArgumentException("Invalid work type: " + jobFormDTO.getWorkType() + ". Must be HYBRID, REMOTE, or ONSITE.");
	    }
	    job.setWorkType(workType);

	    // Parse deadline string to LocalDateTime
	    LocalDateTime dateTime;
	    try {
	        LocalDate localDate = jobFormDTO.getDeadline();
	        dateTime = localDate.atTime(23, 59, 59); // Set to end of day
	    } catch (DateTimeParseException | NullPointerException e) {
	        throw new IllegalArgumentException("Invalid deadline format: " + jobFormDTO.getDeadline() + ". Must be YYYY-MM-DD.");
	    }
	    job.setDeadline(dateTime);

	    job.setJobStatus(JobStatus.open);

	    // Set approval status based on company verification
	    Company company = employer.getCompany();
	    if (!company.isVerified()) {
	        job.setApprovalStatus(ApplicationStatus.pending);
	    } else {
	        job.setApprovalStatus(ApplicationStatus.approved);
	    }

	    // Save the job
	    jobsRepo.saveAndFlush(job);

	    return "Job saved successfully";
	}
	
	public List<JobDisplayDTO> getAllJobs(String jwt) {
        // Validate JWT
        if (jwt == null || jwt.isBlank()) {
            throw new IllegalArgumentException("JWT token is empty or null");
        }

        // Log JWT for debugging
        System.out.println("Received JWT: " + jwt);

        // Extract username from JWT
        String username = jwtutil.extractIdentifier(jwt);
        System.out.println("Extracted username: " + username);

        // Fetch user
        Users user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        // Fetch employer
        Employer employer = employerRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Employer not found for user: " + username));

        // Fetch jobs for employer
        List<Jobs> jobs = jobsRepo.findByEmployer(employer);

        // Map to JobDisplayDTO
        return jobs.stream()
                .map(JobDisplayDTO::new)
                .toList();
    }

    public List<JobDisplayDTO> getTop2ActiveJobs(String jwt) {
        // Validate JWT
        if (jwt == null || jwt.isBlank()) {
            throw new IllegalArgumentException("JWT token is empty or null");
        }

        // Extract username from JWT
        String username = jwtutil.extractIdentifier(jwt);
        Users user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        // Fetch employer
        Employer employer = employerRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Employer not found for user: " + username));

        // Fetch top 2 active jobs (not expired, ordered by posted date)
        List<Jobs> jobs = jobsRepo.findTop2ByEmployerAndJobStatusNotAndDeadlineAfterOrderByPostedDateDesc(
                employer, JobStatus.expired, LocalDateTime.now());

        // Map to JobDisplayDTO
        return jobs.stream()
                .map(JobDisplayDTO::new)
                .toList();
    }
}
