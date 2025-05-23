package com.talenttap.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talenttap.DTO.EditJob;
import com.talenttap.DTO.AdminJobDTO;
import com.talenttap.DTO.CandidatesDTO;
import com.talenttap.DTO.EmploymentTypeDTO;
import com.talenttap.DTO.JobCategoryDTO;
import com.talenttap.DTO.JobDTO;
import com.talenttap.DTO.JobDisplayDTO;
import com.talenttap.DTO.JobFormDTO;
import com.talenttap.DTO.LocationDTO;
import com.talenttap.entity.ApplicationStatus;
import com.talenttap.entity.Company;
import com.talenttap.entity.Employer;
import com.talenttap.entity.EmploymentType;
import com.talenttap.entity.JobApplication;
import com.talenttap.entity.JobCategory;
import com.talenttap.entity.JobStatus;
import com.talenttap.entity.Jobs;
import com.talenttap.entity.Location;
import com.talenttap.entity.SalaryRange;
import com.talenttap.entity.Skills;
import com.talenttap.entity.Users;
import com.talenttap.entity.WorkType;
import com.talenttap.exceptions.EmployerNotFoundException;
import com.talenttap.exceptions.InvalidJwtException;
import com.talenttap.repository.EmployerRepository;
import com.talenttap.repository.EmploymentTypeRepository;
import com.talenttap.repository.JobApplicationRepository;
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
	
	private JobApplicationRepository jobApplicationRepo;
	
	private static final Logger logger = LoggerFactory.getLogger(JobService.class);

	public JobService(EmploymentTypeRepository employmentTypeRepo, JobCategoryRepository jobCategoryRepository,
			JwtUtil jwtutil, UsersRepository userRepo, EmployerRepository employerRepo, LocationRepository locationRepo,
			SkillsRepository skillsRepo, JobsRepository jobsRepo, SalaryRangeRepository salaryRangeRepo, JobApplicationRepository jobApplicationRepo) {
		this.employmentTypeRepo = employmentTypeRepo;
		this.jobCategoryRepository = jobCategoryRepository;
		this.jwtutil = jwtutil;
		this.userRepo = userRepo;
		this.employerRepo = employerRepo;
		this.locationRepo = locationRepo;
		this.skillsRepo = skillsRepo;
		this.jobsRepo = jobsRepo;
		this.salaryRangeRepo = salaryRangeRepo;
		this.jobApplicationRepo = jobApplicationRepo;
	}

	public List<EmploymentTypeDTO> getAllEmploymentType() {
		List<EmploymentTypeDTO> list = employmentTypeRepo.findAll().stream().map(EmploymentTypeDTO::new).toList();
		return list;
	}

	public List<JobCategoryDTO> getAllJobCategory() {
		List<JobCategoryDTO> jobCategory = jobCategoryRepository.findAll().stream().map(JobCategoryDTO::new).toList();
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
			throw new IllegalArgumentException(
					"Invalid work type: " + jobFormDTO.getWorkType() + ". Must be HYBRID, REMOTE, or ONSITE.");
		}
		job.setWorkType(workType);

		// Parse deadline string to LocalDateTime
		LocalDateTime dateTime;
		try {
			LocalDate localDate = jobFormDTO.getDeadline();
			dateTime = localDate.atTime(23, 59, 59); // Set to end of day
		} catch (DateTimeParseException | NullPointerException e) {
			throw new IllegalArgumentException(
					"Invalid deadline format: " + jobFormDTO.getDeadline() + ". Must be YYYY-MM-DD.");
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

	@Transactional
	public List<JobDisplayDTO> getAllJobs(String jwt) {
	    if (jwt == null || jwt.isBlank()) {
	        logger.error("JWT token is empty or null");
	        throw new InvalidJwtException("JWT token is empty or null");
	    }

	    logger.info("Received getAllJobs request with JWT token");

	    String username = jwtutil.extractIdentifier(jwt);
	    logger.debug("Extracted username from JWT: {}", username);

	    Users user = userRepo.findByUsername(username)
	            .orElseThrow(() -> {
	                logger.error("User not found for username: {}", username);
	                return new EmployerNotFoundException("User not found: " + username);
	            });

	    Employer employer = employerRepo.findByUser(user)
	            .orElseThrow(() -> {
	                logger.error("Employer not found for user: {}", username);
	                return new EmployerNotFoundException("Employer not found for user: " + username);
	            });

	    logger.info("Marking expired jobs as EXPIRED for employer: {}", username);
	    int updatedCount = jobsRepo.markExpiredJobs(JobStatus.expired, LocalDateTime.now(), employer);
	    logger.info("Marked {} jobs as EXPIRED for employer {}", updatedCount, username);

	    List<Jobs> jobs = jobsRepo.findByEmployer(employer);

	    logger.info("Returning {} job(s) for employer {}", jobs.size(), username);
	    
	    List<JobDisplayDTO> jobs1 = jobs.stream().map(JobDisplayDTO::new).toList();
	    
	    for (JobDisplayDTO j : jobs1) {
			j.setApplicants(jobApplicationRepo.countByJob_JobId(j.getJobId()));
		}

	    return jobs1;
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
		List<Jobs> jobs = jobsRepo.findTop2ByEmployerAndJobStatusNotAndDeadlineAfterOrderByPostedDateDesc(employer,
				JobStatus.expired, LocalDateTime.now());

		// Map to JobDisplayDTO
		 List<JobDisplayDTO> jobs1 = jobs.stream().map(JobDisplayDTO::new).toList();
		    
		    for (JobDisplayDTO j : jobs1) {
				j.setApplicants(jobApplicationRepo.countByJob_JobId(j.getJobId()));
			}
		return jobs1;
	}

	public Jobs changeJobStatus(int id) {
		Jobs job = jobsRepo.findById(id).get();
		JobStatus status = job.getJobStatus();
		job.setJobStatus(status == JobStatus.open ? JobStatus.closed : JobStatus.open);
		return jobsRepo.save(job);
	}

	public boolean deleteJob(int id) {
		Optional<Jobs> jobOpt = jobsRepo.findById(id);

		if (jobOpt.isEmpty()) {
			logger.warn("Job with ID {} not found.", id);
			return false;
		}

		Jobs job = jobOpt.get();

		// Clear ManyToMany relations first
		job.getRequiredSkills().clear();
		job.getJobLocation().clear();

		jobsRepo.save(job);

		jobsRepo.delete(job);
		// logger.info("Job with ID {} deleted successfully.", id);
		return true;
	}

	public String editJob(EditJob dto) {
		Jobs job = jobsRepo.findById(dto.getJobId()).get();

		// Fetch job category and employment type
		JobCategory jobCategory = jobCategoryRepository.findById(dto.getJobCategoryId())
				.orElseThrow(() -> new RuntimeException("Job category not found: " + dto.getJobCategoryId()));
		EmploymentType employmentType = employmentTypeRepo.findById(dto.getJobTypeId())
				.orElseThrow(() -> new RuntimeException("Employment type not found: " + dto.getJobTypeId()));

		job.setJobRole(dto.getJobRole());
		job.setJobCategory(jobCategory);
		job.setJobType(employmentType);
		job.setJobDescription(dto.getJobDescription());

		// Add skills
		for (Integer skillId : dto.getSkillIds()) {
			job.getRequiredSkills().add(skillsRepo.findById(skillId)
					.orElseThrow(() -> new RuntimeException("Skill not found: " + skillId)));
		}

		// Add locations
		for (Integer locationId : dto.getLocationIds()) {
			job.getJobLocation().add(locationRepo.findById(locationId)
					.orElseThrow(() -> new RuntimeException("Location not found: " + locationId)));
		}

		// Handle SalaryRange
		SalaryRange salary = new SalaryRange();
		salary.setMax_range(dto.getSalaryMax() != null ? dto.getSalaryMax() : 0.0);
		salary.setMin_range(dto.getSalaryMin() != null ? dto.getSalaryMin() : 0.0);
		// Save and flush SalaryRange to ensure it's persisted
		salary = salaryRangeRepo.saveAndFlush(salary);
		job.setSalary_range(salary);

		// Set other job fields
		job.setRoles(dto.getRequirements());
		job.setResponsibilities(dto.getResponsibilities());
		job.setBenefits(dto.getBenefits());
		job.setOpenings(dto.getOpenings());
		job.setStipend(dto.getStipend() != null ? dto.getStipend() : 0.0);
		job.setDuration(dto.getDuration() != null ? dto.getDuration() : 0);
		job.setYearsOfExperience(dto.getYearsOfExperience());
		job.setPostedDate(LocalDateTime.now());

		// Convert workType string to WorkType enum
		WorkType workType;
		try {
			workType = WorkType.valueOf(dto.getWorkType().toUpperCase());
		} catch (IllegalArgumentException | NullPointerException e) {
			throw new IllegalArgumentException(
					"Invalid work type: " + dto.getWorkType() + ". Must be HYBRID, REMOTE, or ONSITE.");
		}
		job.setWorkType(workType);

		// Parse deadline string to LocalDateTime
		LocalDateTime dateTime;
		try {
			String localDate = dto.getDeadline();
			LocalDate dtp = LocalDate.parse(localDate);
			dateTime = dtp.atTime(23, 59, 59); // Set to end of day
		} catch (DateTimeParseException | NullPointerException e) {
			throw new IllegalArgumentException(
					"Invalid deadline format: " + dto.getDeadline() + ". Must be YYYY-MM-DD.");
		}
		job.setDeadline(dateTime);

		job.setJobStatus(JobStatus.open);

		// Save the job
		jobsRepo.saveAndFlush(job);

		return "job edited successfully";

	}

	public Jobs getJobById(int id) {
		// TODO Auto-generated method stub
		Jobs job = jobsRepo.findById(id).get();
		return job;
	}

	public Page<Jobs> searchJobs(String keyword, Pageable pageable) {
		return jobsRepo.searchByKeyword(keyword, pageable);
	}

	
	public List<CandidatesDTO> getAllAppliedCandidates(String jwt) {
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
				
				List<JobApplication> application = jobApplicationRepo.findByJob_Employer(employer);
				List<CandidatesDTO> candidates = application.stream().map(CandidatesDTO::new).toList();
		
		return candidates;
	}
	
	// Admin section
		// Jobs to display in admins page
		public List<AdminJobDTO> getAllJobsAdmin(String jwt) {
			// Validate JWT
			if (jwt == null || jwt.isBlank()) {
				throw new IllegalArgumentException("JWT token is empty or null");
			}

			// Extract username from JWT
			String username = jwtutil.extractIdentifier(jwt);
			Users user = userRepo.findByUsername(username)
					.orElseThrow(() -> new RuntimeException("User not found: " + username));

			// Fetch top 2 active jobs (not expired, ordered by posted date)
			List<Jobs> jobs = jobsRepo.findAll();

			// Map to JobDisplayDTO
			return jobs.stream().map(job -> {
				AdminJobDTO dto = new AdminJobDTO();

				dto.setJobId(job.getJobId());
				dto.setJobRole(job.getJobRole());
				dto.setJobType(job.getJobType().getEmploymentType());
				dto.setJobCategory(job.getJobCategory().getJobCategory());
				dto.setJobDescription(job.getJobDescription());
				dto.setRoles(job.getRoles());
				dto.setResponsibilities(job.getResponsibilities());
				dto.setBenefits(job.getBenefits());
				dto.setDuration(job.getDuration() != 0 ? job.getDuration() : null);
				dto.setStipend(job.getStipend() != 0 ? job.getStipend() : null);

				if (job.getSalary_range() != null) {
					dto.setSalaryMin(job.getSalary_range().getMin_range());
					dto.setSalaryMax(job.getSalary_range().getMax_range());
				}

				dto.setYearsOfExperience(job.getYearsOfExperience());
				dto.setWorkType(job.getWorkType().name());
				dto.setOpenings(job.getOpenings());
				dto.setPostedDate(job.getPostedDate());
				dto.setDeadline(job.getDeadline());
				dto.setJobStatus(job.getJobStatus().name());
				dto.setApprovalStatus(job.getApprovalStatus().name());
				dto.setExpired(job.getDeadline().isBefore(LocalDateTime.now()));

				// Extract skills
				dto.setRequiredSkills(job.getRequiredSkills().stream().map(Skills::getSkill).collect(Collectors.toSet()));

				// Extract locations
				dto.setLocations(job.getJobLocation().stream().map(Location::getLocation).collect(Collectors.toSet()));

				// Add company name from employer → user → company name
				dto.setCompanyName(job.getEmployer().getCompany().getCompanyName());

				// Add applicants count (if you have job.getApplications())
				dto.setApplicants(0);

				return dto;
			}).collect(Collectors.toList());
		}

		 public AdminJobDTO getJobById(int jobId, String jwt) {
	         if (jwt == null || jwt.isBlank()) {
	             throw new IllegalArgumentException("JWT token is empty or null");
	         }

	         String username = jwtutil.extractIdentifier(jwt);
	         Users user = userRepo.findByUsername(username)
	                 .orElseThrow(() -> new RuntimeException("User not found: " + username));

	         Optional<Jobs> jobOpt = jobsRepo.findById(jobId);
	         if (jobOpt.isEmpty()) {
	             return null;
	         }

	         Jobs job = jobOpt.get();
	         AdminJobDTO dto = new AdminJobDTO();

	         dto.setJobId(job.getJobId());
	         dto.setJobRole(job.getJobRole());
	         dto.setJobType(job.getJobType().getEmploymentType());
	         dto.setJobCategory(job.getJobCategory().getJobCategory());
	         dto.setJobDescription(job.getJobDescription());
	         dto.setRoles(job.getRoles());
	         dto.setResponsibilities(job.getResponsibilities());
	         dto.setBenefits(job.getBenefits());
	         dto.setDuration(job.getDuration() != 0 ? job.getDuration() : null);
	         dto.setStipend(job.getStipend() != 0 ? job.getStipend() : null);

	         if (job.getSalary_range() != null) {
	             dto.setSalaryMin(job.getSalary_range().getMin_range());
	             dto.setSalaryMax(job.getSalary_range().getMax_range());
	         }

	         dto.setYearsOfExperience(job.getYearsOfExperience());
	         dto.setWorkType(job.getWorkType().name());
	         dto.setOpenings(job.getOpenings());
	         dto.setPostedDate(job.getPostedDate());
	         dto.setDeadline(job.getDeadline());
	         dto.setJobStatus(job.getJobStatus().name());
	         dto.setApprovalStatus(job.getApprovalStatus().name());
	         dto.setExpired(job.getDeadline().isBefore(LocalDateTime.now()));

	         dto.setRequiredSkills(job.getRequiredSkills()
	                 .stream()
	                 .map(Skills::getSkill)
	                 .collect(Collectors.toSet()));

	         dto.setLocations(job.getJobLocation()
	                 .stream()
	                 .map(Location::getLocation)
	                 .collect(Collectors.toSet()));

	         dto.setCompanyName(job.getEmployer().getCompany().getCompanyName());

	         dto.setApplicants(jobApplicationRepo.countByJob_JobId(jobId));

	         return dto;
	     }

	 	 public void approveJobs(List<Integer> jobIds, String jwt) {
	 		 if (jwt == null || jwt.isBlank()) {
	 			 throw new IllegalArgumentException("JWT token is empty or null");
	 		 }
	 		 
	 		 String username = jwtutil.extractIdentifier(jwt);
	 		 Users user = userRepo.findByUsername(username)
	 				 .orElseThrow(() -> new RuntimeException("User not found: " + username));
	 		 
	 		 for (Integer jobId : jobIds) {
	 			 Jobs job = jobsRepo.findById(jobId)
	 					 .orElseThrow(() -> new RuntimeException("Job not found: " + jobId));
	 			 job.setApprovalStatus(ApplicationStatus.approved);
	 			 jobsRepo.save(job);
	 		 }
	 	 }
	 	 
	 	public void rejectJobs(List<Integer> jobIds, String jwt) {
	 	    if (jwt == null || jwt.isBlank()) {
	 	        throw new IllegalArgumentException("JWT token is empty or null");
	 	    }

	 	    String username = jwtutil.extractIdentifier(jwt);
	 	    Users user = userRepo.findByUsername(username)
	 	            .orElseThrow(() -> new RuntimeException("User not found: " + username));

	 	    for (Integer jobId : jobIds) {
	 	        Jobs job = jobsRepo.findById(jobId)
	 	                .orElseThrow(() -> new RuntimeException("Job not found: " + jobId));
	 	        job.setApprovalStatus(ApplicationStatus.rejected);
	 	        jobsRepo.save(job);
	 	    }
	 	}

}
