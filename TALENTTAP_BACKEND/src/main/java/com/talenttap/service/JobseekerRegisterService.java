package com.talenttap.service;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.talenttap.DTO.AddEducationDTO;
import com.talenttap.DTO.Certifications;
import com.talenttap.DTO.EducationDTO;
import com.talenttap.DTO.ExperienceDTO;
import com.talenttap.DTO.JobDTO;
import com.talenttap.DTO.JobFilterDTO;
import com.talenttap.DTO.JobseekerDTO;
import com.talenttap.DTO.JobseekerRegisterDTO;
import com.talenttap.DTO.Languages;
import com.talenttap.DTO.ProjectDTO;
import com.talenttap.DTO.SkillsDTO;
import com.talenttap.entity.ApplicationStatus;
import com.talenttap.entity.AuthProvider;
import com.talenttap.entity.Certification;
import com.talenttap.entity.Education;
import com.talenttap.entity.Experience;
import com.talenttap.entity.Gender;
import com.talenttap.entity.JobApplication;
import com.talenttap.entity.Users;
import com.talenttap.exceptions.InvalidCredentialsException;
import com.talenttap.entity.JobSeeker;
import com.talenttap.entity.Jobs;
import com.talenttap.entity.Language;
import com.talenttap.entity.Project;
import com.talenttap.entity.Skills;
import com.talenttap.repository.CertificationRepository;
import com.talenttap.repository.EducationLevelRepository;
import com.talenttap.repository.EducationRepository;
import com.talenttap.repository.EmploymentTypeRepository;
import com.talenttap.repository.ExperienceRepository;
import com.talenttap.repository.JobApplicationRepository;
import com.talenttap.repository.JobsRepository;
import com.talenttap.repository.JobseekerRepository;
import com.talenttap.repository.LanguageRepository;
import com.talenttap.repository.LocationRepository;
import com.talenttap.repository.ProjectRepository;
import com.talenttap.repository.RoleRepository;
import com.talenttap.repository.SkillsRepository;
import com.talenttap.repository.UsersRepository;
import com.talenttap.security.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Service
public class JobseekerRegisterService {

	@Autowired
	private JwtUtil jwtUtil;

	private UsersRepository userRepo;
	private JobseekerRepository jobseekerRepo;
	private EducationRepository educationRepo;
	private CertificationRepository certificationRepo;
	private SkillsRepository skillsRepo;
	private RoleRepository roleRepo;
	private LocationRepository locationRepo;
	private EducationLevelRepository educationLevelRepo;
	private PasswordEncoder passwordEncoder;
	private JobsRepository jobRepo;
	private JobApplicationRepository jobApplicationRepo;
	private LanguageRepository languageRepo;
	private ProjectRepository projectRepo;
	private EmploymentTypeRepository employmentTypeRepo;
	private ExperienceRepository experienceRepo;

	public JobseekerRegisterService(UsersRepository userRepo, JobseekerRepository jobseekerRepo,
			EducationRepository educationRepo, SkillsRepository skillsRepo, RoleRepository roleRepo,
			LocationRepository locationRepo, EducationLevelRepository educationLevelRepo,
			PasswordEncoder passwordEncoder, JobsRepository jobRepo, JobApplicationRepository jobApplicationRepo,
			CertificationRepository certificationRepo, LanguageRepository languageRepo, ProjectRepository projectRepo,
			EmploymentTypeRepository employmentTypeRepo ,ExperienceRepository experienceRepo) {
		this.educationRepo = educationRepo;
		this.jobseekerRepo = jobseekerRepo;
		this.employmentTypeRepo = employmentTypeRepo;
		this.skillsRepo = skillsRepo;
		this.userRepo = userRepo;
		this.locationRepo = locationRepo;
		this.roleRepo = roleRepo;
		this.educationLevelRepo = educationLevelRepo;
		this.passwordEncoder = passwordEncoder;
		this.jobRepo = jobRepo;
		this.jobApplicationRepo = jobApplicationRepo;
		this.certificationRepo = certificationRepo;
		this.languageRepo = languageRepo;
		this.projectRepo = projectRepo;
		this.experienceRepo = experienceRepo;
	}

	public ResponseEntity<?> register(@Valid JobseekerRegisterDTO request) {
		Users user = new Users();
		user.setRole(roleRepo.findByRole("JOBSEEKER"));
		user.setAuthProvider(AuthProvider.MANUAL);
		user.setDateOfRegistration(LocalDate.now());
		user.setEmail(request.getEmail());
		user.setFullName(request.getFullName());
		user.setGoogleId(null);
		user.setMobileNumber(request.getPhoneNumber());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setUsername(request.getUsername());

		user = userRepo.save(user);

		JobSeeker jobseeker = new JobSeeker();
		jobseeker.setLocation(locationRepo.findById(request.getLocation()).get());
		jobseeker.setUser(user);
		jobseeker.setYearOfExperience(request.getYearsOfExperience());

		for (Integer skillId : request.getSkillIds()) {
			jobseeker.getSeekerSkills().add(skillsRepo.findById(skillId).get());
		}

		jobseeker = jobseekerRepo.save(jobseeker);

		Education education = new Education();
		education.setJobSeeker(jobseeker);
		education.setEducationLevel(educationLevelRepo.findById(request.getHighestQualification()).get());
		education.setInstitution(request.getInstitution());
		education.setBoardOfStudy(request.getBoardOfStudy());
		education.setDegree(request.getDegree());
		education.setPercentage(request.getPercentage());
		education.setEndYear(request.getEndYear());
		education.setStartYear(request.getStartYear());

		educationRepo.save(education);

		return ResponseEntity.ok().body("Jobseeker Registered Successfully!");
	}

	public ResponseEntity<?> login(String username, String password, HttpServletResponse response) {

		Users user = userRepo.findByUsernameOrEmail(username, username).get();

		if (user != null && passwordEncoder.matches(password, user.getPassword())) {
			String jwt = jwtUtil.generateToken(user.getUsername(), "JOBSEEKER");

			System.out.println(jwt);
			Cookie cookie = new Cookie("jwt", jwt);
			cookie.setHttpOnly(true);
			cookie.setPath("/");
			cookie.setSecure(false);
			cookie.setMaxAge(24 * 60 * 60);
			response.addCookie(cookie);

			return ResponseEntity.ok().body("User Logged in Successfully");
		} else {
			throw new InvalidCredentialsException("Invalid Username/Password!");
		}
	}

	public ResponseEntity<String> getFullName(String jwt) {

		if (jwt == null || jwt.isBlank() || jwt.isEmpty()) {
			return ResponseEntity.badRequest().body("Jwt Token is Empty!");
		}
		String username = jwtUtil.extractIdentifier(jwt);
		Users user = userRepo.findByUsername(username).get();

		return ResponseEntity.ok().body(user.getFullName());
	}

	public ResponseEntity<JobseekerDTO> getJobseeker(String token) {

		String username = jwtUtil.extractIdentifier(token);

		Optional<Users> optionalUser = userRepo.findByUsername(username);
		if (optionalUser.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Users user = optionalUser.get();

		Optional<JobSeeker> optionalJobseeker = jobseekerRepo.findByUser(user);
		if (optionalJobseeker.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		JobSeeker jobseeker = optionalJobseeker.get();
		JobseekerDTO response = new JobseekerDTO(jobseeker, user);

		return ResponseEntity.ok(response);
	}

	public ResponseEntity<?> getProfilePhotoById(int id) {
		JobSeeker jobSeeker = jobseekerRepo.findById(id).orElse(null);

		if (jobSeeker == null || jobSeeker.getProfilePhoto() == null) {
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(URI.create("http://localhost:8082/img/default_profile.png"));
			return new ResponseEntity<>(headers, HttpStatus.FOUND);
		}

		byte[] imageBytes = jobSeeker.getProfilePhoto();
		String contentType = detectImageType(imageBytes);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(contentType));

		return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
	}

	private String detectImageType(byte[] imageBytes) {
		if (imageBytes == null || imageBytes.length < 4) {
			return "image/png";
		}

		if (imageBytes[0] == (byte) 0xFF && imageBytes[1] == (byte) 0xD8) {
			return "image/jpeg";
		} else if (imageBytes[0] == (byte) 0x89 && imageBytes[1] == (byte) 0x50) {
			return "image/png";
		} else {
			return "image/png";
		}
	}

	public ResponseEntity<String> uploadProfilePicture(MultipartFile file, Integer jobSeekerId) {

		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body("No file uploaded");
		}

		try {
			JobSeeker jobSeeker = jobseekerRepo.findById(jobSeekerId).orElse(null);
			if (jobSeeker != null) {
				jobSeeker.setProfilePhoto(file.getBytes());
				jobseekerRepo.save(jobSeeker);
				return ResponseEntity.ok("Profile photo updated");
			} else {
				return ResponseEntity.badRequest().body("Job Seeker not found");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading photo");
		}
	}

	public ResponseEntity<String> updateProfile(JobseekerDTO request) {

		try {
			JobSeeker jobseeker = jobseekerRepo.findById(request.getId()).get();
			System.out.println(jobseeker.getJobSeekerId());
			Users user = userRepo.findById(jobseeker.getUser().getUserId()).get();

			user.setEmail(request.getEmail());
			user.setFullName(request.getFullName());
			user.setMobileNumber(request.getPhone());
			user.setUsername(request.getUsername());
			if (request.getPassword() != null && !request.getPassword().isBlank()) {
				user.setPassword(passwordEncoder.encode(request.getPassword()));
			}
			user = userRepo.save(user);

			jobseeker.setUser(user);

			if (request.getDob() != null) {
				jobseeker.setDob(request.getDob());
			}

			jobseeker.setLocation(locationRepo.findByLocation(request.getLocation()).get());

			if (request.getGender() != null && !request.getGender().isBlank() && !request.getGender().isEmpty()) {
				Gender gender = Gender.valueOf(request.getGender().toUpperCase());
				jobseeker.setGender(gender);

			}

			jobseekerRepo.save(jobseeker);

			return ResponseEntity.ok("Updated Succsessfully!");
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body("failed to update profile!");
		}

	}

	public ResponseEntity<String> updateSummary(String summary, int id) {
		JobSeeker jobseeker = jobseekerRepo.findById(id).get();

		jobseeker.setProfileSummary(summary);
		jobseekerRepo.save(jobseeker);

		return ResponseEntity.ok().body("Updated summary successfully!");

	}

	public ResponseEntity<List<JobDTO>> getAllJobs() {

		List<JobDTO> jobs = jobRepo.findAll().stream().map(JobDTO::new).toList();

		for (JobDTO j : jobs) {
			j.setApplicants(jobApplicationRepo.countByJob_JobId(j.getJobId()));
		}

		return ResponseEntity.ok(jobs);

	}

	public ResponseEntity<JobDTO> getJobById(int id) {

		JobDTO job = new JobDTO(jobRepo.findById(id).get());
		job.setApplicants(jobApplicationRepo.countByJob_JobId(job.getJobId()));

		return ResponseEntity.ok(job);
	}

	public ResponseEntity<List<JobDTO>> filterJobs(JobFilterDTO jobFilter) {

		List<Jobs> jobs = jobRepo.findAll();

		if (jobs != null) {
			if (jobFilter.getExperience() != null && !jobFilter.getExperience().isBlank()) {
				ExperienceRange filterRange = new ExperienceRange(jobFilter.getExperience());

				jobs = jobs.stream().filter(job -> {
					ExperienceRange jobRange = new ExperienceRange(job.getYearsOfExperience());
					return jobRange.overlapsWith(filterRange);
				}).toList();
			}
			if (jobFilter.getCategory() != null) {
				jobs = jobs.stream().filter(x -> x.getJobCategory().getJobCategoryId() == jobFilter.getCategory())
						.toList();
			}
			if (jobFilter.getLocation() != null) {
				jobs = jobs.stream().filter(x -> x.getJobLocation().stream()
						.anyMatch(loc -> loc.getLocationId() == jobFilter.getLocation())).toList();
			}
		}

		List<JobDTO> jobsDTO = jobs.stream().map(JobDTO::new).toList();

		if (jobsDTO != null) {

			if (jobFilter.getDuration() != null) {
				jobsDTO = jobsDTO.stream().filter(x -> x.getDuration() == jobFilter.getDuration()).toList();
			}

			if (jobFilter.getFreshness() != null && jobFilter.getFreshness() != 0) {
				jobsDTO = jobsDTO.stream()
						.filter(x -> x.getPostedAgo().contains("days")
								&& Integer.valueOf(x.getPostedAgo().split(" ")[0]) <= jobFilter.getFreshness())
						.toList();
			}

			if (jobFilter.getFreshness() != null && jobFilter.getFreshness() == 0) {
				jobsDTO = jobsDTO.stream().filter(x -> x.getPostedAgo().contains("hours")).toList();
			}

			if (jobFilter.getJobType() != null && !jobFilter.getJobType().isBlank()) {
				jobsDTO = jobsDTO.stream().filter(x -> x.getJobType().equals(jobFilter.getJobType())).toList();
			}

			if (jobFilter.getSalary() != null && !jobFilter.getSalary().isBlank()) {
				jobsDTO = jobsDTO.stream().filter(x -> x.getSalaryRange().split(" ")[0].trim()
						.contains(jobFilter.getSalary().split(" ")[0].trim())
						&& x.getSalaryRange().split(" ")[2].trim().contains(jobFilter.getSalary().split(" ")[2].trim()))
						.toList();
			}

			if (jobFilter.getStipend() != null) {
				jobsDTO = jobsDTO.stream().filter(x -> x.getStipend().equals(jobFilter.getStipend())).toList();
			}
		}
		return ResponseEntity.ok().body(jobsDTO);
	}

	public ResponseEntity<List<EducationDTO>> getAllEducation(Integer id) {
		List<EducationDTO> educations = educationRepo.findByJobSeeker_JobSeekerId(id).stream().map(EducationDTO::new)
				.toList();
		return ResponseEntity.ok().body(educations);
	}

	public ResponseEntity<List<SkillsDTO>> getAllSkillsById(Integer id) {
		List<SkillsDTO> skills = jobseekerRepo.findById(id).get().getSeekerSkills().stream().map(SkillsDTO::new)
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(skills);
	}

	public ResponseEntity<String> deleteSkillById(Integer id, Integer jobseekerId) {
		JobSeeker jobSeeker = jobseekerRepo.findById(jobseekerId).get();

		Skills skill = skillsRepo.findById(id).get();

		if (jobSeeker != null && skill != null && jobSeeker.getSeekerSkills().contains(skill)) {
			jobSeeker.getSeekerSkills().remove(skill);
			jobseekerRepo.save(jobSeeker);
			return ResponseEntity.ok().body("Skill deleted successfully!");
		}
		return ResponseEntity.notFound().build();
	}

	public ResponseEntity<String> uploadResume(MultipartFile file, String jwt) {
		if (jwt == null || jwt.trim().isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
		}

		try {
			byte[] resumeBytes = file.getBytes();
			if (jwt == null || jwt.isBlank() || jwt.isEmpty()) {
				return ResponseEntity.badRequest().body("Jwt Token is Empty!");
			}
			String username = jwtUtil.extractIdentifier(jwt);
			Users user = userRepo.findByUsername(username).get();

			JobSeeker jobseeker = jobseekerRepo.findByUser(user).get();

			jobseeker.setResume(resumeBytes);
			jobseekerRepo.save(jobseeker);

			return ResponseEntity.ok("Resume uploaded successfully");

		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload resume");
		}
	}

	public ResponseEntity<?> getResume(String jwt) {
		if (jwt != null && !jwt.trim().isEmpty()) {
			if (jwt == null || jwt.isBlank() || jwt.isEmpty()) {
				return ResponseEntity.badRequest().body("Jwt Token is Empty!");
			}
			String username = jwtUtil.extractIdentifier(jwt);
			Users user = userRepo.findByUsername(username).get();

			JobSeeker jobseeker = jobseekerRepo.findByUser(user).get();

			byte[] resume = jobseeker.getResume();
			if (resume != null) {
				return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(resume);
			}
		}
		return ResponseEntity.notFound().build();
	}

	public ResponseEntity<Void> deleteResume(String jwt) {
		if (jwt != null && !jwt.trim().isEmpty()) {

			String username = jwtUtil.extractIdentifier(jwt);
			Users user = userRepo.findByUsername(username).get();

			JobSeeker jobseeker = jobseekerRepo.findByUser(user).get();

			if (jobseeker != null && jobseeker.getResume() != null) {
				jobseeker.setResume(null);
				jobseekerRepo.save(jobseeker);
				return ResponseEntity.ok().build();
			}
		}
		return ResponseEntity.notFound().build();
	}

	public ResponseEntity<String> applyJob(String jwt, int jobId) {
		if (jwt != null && !jwt.trim().isEmpty()) {

			String username = jwtUtil.extractIdentifier(jwt);
			Users user = userRepo.findByUsername(username).get();

			JobSeeker jobseeker = jobseekerRepo.findByUser(user).get();
			Jobs job = jobRepo.findById(jobId).get();

			JobApplication application = new JobApplication();
			application.setDateOfApplication(LocalDateTime.now());
			application.setJob(job);
			application.setJobSeeker(jobseeker);
			application.setLastUpdated(LocalDateTime.now());
			application.setStatus(ApplicationStatus.pending);

			jobApplicationRepo.save(application);

			return ResponseEntity.ok().body("Job Applied Successfully!");
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	public ResponseEntity<Boolean> hasApplied(String jwt, int jobId) {

		boolean hasApplied = false;
		if (jwt != null && !jwt.trim().isEmpty()) {

			String username = jwtUtil.extractIdentifier(jwt);
			Users user = userRepo.findByUsername(username).get();

			JobSeeker jobseeker = jobseekerRepo.findByUser(user).get();

			if (jobApplicationRepo.existsByJobSeekerAndJob_JobId(jobseeker, jobId)) {
				hasApplied = true;
			}

		}

		return ResponseEntity.ok(hasApplied);
	}

	public ResponseEntity<?> existByUsername(String username) {
		boolean exists = false;
		if (userRepo.existsByUsername(username)) {
			exists = true;
		}
		return ResponseEntity.ok().body(Map.of("exists", exists));
	}

	public ResponseEntity<?> existByEmail(String email) {
		boolean exists = false;
		if (userRepo.existsByEmail(email)) {
			exists = true;
		}
		return ResponseEntity.ok().body(Map.of("exists", exists));
	}

	public ResponseEntity<String> addEducation(int id, AddEducationDTO request) {
		try {
			JobSeeker jobseeker = jobseekerRepo.findById(id).get();

			Education education = new Education();
			education.setJobSeeker(jobseeker);
			education.setEducationLevel(educationLevelRepo.findById(request.getHighestQualification()).get());
			education.setInstitution(request.getInstitution());
			if (request.getBoardOfStudy().isBlank() || request.getBoardOfStudy().isEmpty()) {
				education.setBoardOfStudy(null);
			} else {
				education.setBoardOfStudy(request.getBoardOfStudy());
			}

			if (request.getDegree().isBlank() || request.getDegree().isEmpty()) {
				education.setDegree(null);
			} else {
				education.setDegree(request.getDegree());
			}
			education.setPercentage(request.getPercentage());
			education.setEndYear(request.getEndYear());
			education.setStartYear(request.getStartYear());

			educationRepo.save(education);

			return ResponseEntity.ok().body("Education added Successfully!");
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	public ResponseEntity<String> deleteEducation(int id) {
		try {
			educationRepo.deleteById(id);
			return ResponseEntity.ok().body("Education deleted Successfully!");
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	public ResponseEntity<String> updateEducation(int id, EducationDTO dto) {
		try {
			Education education = educationRepo.findById(id)
					.orElseThrow(() -> new RuntimeException("Education not found"));

			education.setEducationLevel(educationLevelRepo.findByEducationLevel(dto.getEducationLevel()).orElse(null));
			education.setInstitution(dto.getInstitution());
			if (dto.getBoardOfStudy().isBlank() || dto.getBoardOfStudy().isEmpty()) {
				education.setBoardOfStudy(null);
			} else {
				education.setBoardOfStudy(dto.getBoardOfStudy());
			}

			if (dto.getDegree().isBlank() || dto.getDegree().isEmpty()) {
				education.setDegree(null);
			} else {
				education.setDegree(dto.getDegree());
			}
			education.setStartYear(dto.getStartYear());
			education.setEndYear(dto.getEndYear());
			education.setPercentage(dto.getPercentage());

			educationRepo.save(education);
			return ResponseEntity.ok("Education updated successfully!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating education: " + e.getMessage());
		}
	}

	public ResponseEntity<String> addSkill(int id, List<Integer> skillIds) {

		try {
			Optional<JobSeeker> optionalJobSeeker = jobseekerRepo.findById(id);
			if (optionalJobSeeker.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("JobSeeker not found with ID: " + id);
			}

			JobSeeker jobseeker = optionalJobSeeker.get();

			for (Integer skillId : skillIds) {
				Optional<Skills> optionalSkill = skillsRepo.findById(skillId);
				if (optionalSkill.isPresent()) {
					Skills skill = optionalSkill.get();

					if (!jobseeker.getSeekerSkills().contains(skill)) {
						jobseeker.getSeekerSkills().add(skill);
					}
				} else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Skill ID: " + skillId);
				}
			}

			jobseekerRepo.save(jobseeker);
			return ResponseEntity.ok("Skills added successfully!");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error adding skills: " + e.getMessage());
		}
	}

	public ResponseEntity<List<Certifications>> getAllCertifications(Integer id) {
		List<Certifications> certifications = certificationRepo.findByJobSeeker_JobSeekerId(id).stream()
				.map(Certifications::new).toList();
		return ResponseEntity.ok().body(certifications);
	}

	public ResponseEntity<String> addCertification(int id, Certifications request) {

		try {
			JobSeeker jobseeker = jobseekerRepo.findById(id).get();

			Certification certification = new Certification();
			certification.setJobSeeker(jobseeker);

			if (request.getUrl().isBlank() || request.getUrl().isEmpty())
				certification.setCertificateURL(null);
			else
				certification.setCertificateURL(request.getUrl());

			if (request.getNumber().isBlank() || request.getNumber().isEmpty())
				certification.setCertificationNumber(null);
			else
				certification.setCertificationNumber(request.getNumber());

			if (request.getTitle().isBlank() || request.getTitle().isEmpty())
				certification.setCertificationName(null);
			else
				certification.setCertificationName(request.getTitle());

			if (request.getIssuedBy().isBlank() || request.getIssuedBy().isEmpty())
				certification.setIssuedBy(null);
			else
				certification.setIssuedBy(request.getIssuedBy());

			certification.setExpiryDate(request.getExpiry_date());
			certification.setIssueDate(request.getIssued_date());

			certificationRepo.save(certification);

			return ResponseEntity.ok().body("Certification added Successfully!");
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}

	}

	public ResponseEntity<String> updateCertification(int id, Certifications request) {

		try {

			Certification certification = certificationRepo.findById(id).get();

			if (request.getUrl().isBlank() || request.getUrl().isEmpty())
				certification.setCertificateURL(null);
			else
				certification.setCertificateURL(request.getUrl());

			if (request.getNumber().isBlank() || request.getNumber().isEmpty())
				certification.setCertificationNumber(null);
			else
				certification.setCertificationNumber(request.getNumber());

			if (request.getTitle().isBlank() || request.getTitle().isEmpty())
				certification.setCertificationName(null);
			else
				certification.setCertificationName(request.getTitle());

			if (request.getIssuedBy().isBlank() || request.getIssuedBy().isEmpty())
				certification.setIssuedBy(null);
			else
				certification.setIssuedBy(request.getIssuedBy());

			certification.setExpiryDate(request.getExpiry_date());
			certification.setIssueDate(request.getIssued_date());

			certificationRepo.save(certification);
			return ResponseEntity.ok("Certification updated successfully!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating certifications: " + e.getMessage());
		}
	}

	public ResponseEntity<String> deleteCertification(int id) {
		try {
			certificationRepo.deleteById(id);
			return ResponseEntity.ok().body("certification deleted Successfully!");
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	public ResponseEntity<List<Languages>> getAllLanguages() {
		try {
			List<Languages> languages = languageRepo.findAll().stream().map(Languages::new).toList();
			return ResponseEntity.ok(languages);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	public ResponseEntity<List<Languages>> getAllSeekerLanguage(int id) {

		try {
			JobSeeker jobseeker = jobseekerRepo.findById(id).get();
			List<Languages> languages = jobseeker.getSeekerLanguages().stream().map(Languages::new).toList();
			return ResponseEntity.ok(languages);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	public ResponseEntity<String> addLanguages(int id, List<Integer> languageIds) {

		try {
			Optional<JobSeeker> optionalJobSeeker = jobseekerRepo.findById(id);
			if (optionalJobSeeker.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("JobSeeker not found with ID: " + id);
			}

			JobSeeker jobseeker = optionalJobSeeker.get();

			for (Integer languageId : languageIds) {
				Optional<Language> optionalLanguage = languageRepo.findById(languageId);
				if (optionalLanguage.isPresent()) {
					Language language = optionalLanguage.get();

					if (!jobseeker.getSeekerLanguages().contains(language)) {
						jobseeker.getSeekerLanguages().add(language);
					}
				} else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Language ID: " + languageId);
				}
			}

			jobseekerRepo.save(jobseeker);
			return ResponseEntity.ok("Langauge added successfully!");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error adding Language: " + e.getMessage());
		}
	}

	public ResponseEntity<String> deleteSeekerLanguage(int id, int jobseekerId) {

		try {
			Optional<JobSeeker> optionalJobSeeker = jobseekerRepo.findById(jobseekerId);
			if (optionalJobSeeker.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("JobSeeker not found with ID: " + id);
			}

			JobSeeker jobseeker = optionalJobSeeker.get();

			Language language = languageRepo.findById(id).get();

			if (jobseeker.getSeekerLanguages().contains(language)) {
				jobseeker.getSeekerLanguages().remove(language);
			}
			jobseekerRepo.save(jobseeker);
			return ResponseEntity.ok("language deleted successfully!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error deleting Language: " + e.getMessage());
		}
	}

	public ResponseEntity<List<ProjectDTO>> getAllProjects(Integer id) {
		List<ProjectDTO> projects = projectRepo.findByJobSeeker_JobSeekerId(id).stream().map(ProjectDTO::new).toList();
		return ResponseEntity.ok().body(projects);
	}

	public ResponseEntity<String> addProject(int id, ProjectDTO request) {

		try {
			JobSeeker jobseeker = jobseekerRepo.findById(id).get();

			Project project = new Project();
			project.setJobSeeker(jobseeker);

			if (request.getUrl().isBlank() || request.getUrl().isEmpty())
				project.setProjectURL(null);
			else
				project.setProjectURL(request.getUrl());

			if (request.getTitle().isBlank() || request.getTitle().isEmpty())
				project.setProjectTitle(null);
			else
				project.setProjectTitle(request.getTitle());

			if (request.getDescription().isBlank() || request.getDescription().isEmpty())
				project.setDescription(null);
			else
				project.setDescription(request.getDescription());

			projectRepo.save(project);

			return ResponseEntity.ok().body("Project added Successfully!");
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	public ResponseEntity<String> updateProject(int id, ProjectDTO request) {

		try {

			Project project = projectRepo.findById(id).get();

			if (request.getUrl().isBlank() || request.getUrl().isEmpty())
				project.setProjectURL(null);
			else
				project.setProjectURL(request.getUrl());

			if (request.getTitle().isBlank() || request.getTitle().isEmpty())
				project.setProjectTitle(null);
			else
				project.setProjectTitle(request.getTitle());

			if (request.getDescription().isBlank() || request.getDescription().isEmpty())
				project.setDescription(null);
			else
				project.setDescription(request.getDescription());

			projectRepo.save(project);

			return ResponseEntity.ok().body("Project updated Successfully!");
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}

	}

	public ResponseEntity<String> deleteProject(int id) {
		try {
			projectRepo.deleteById(id);
			return ResponseEntity.ok().body("project deleted Successfully!");
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	public ResponseEntity<String> addExperience(int id, ExperienceDTO request) {
		try {
			JobSeeker jobseeker = jobseekerRepo.findById(id).get();

			Experience experience = new Experience();
			experience.setJobSeeker(jobseeker);

			if (request.getCompany().isBlank() || request.getCompany().isEmpty())
				experience.setCompanyName(null);
			else
				experience.setCompanyName(request.getCompany());

			if (request.getRole().isBlank() || request.getRole().isEmpty())
				experience.setRole(null);
			else
				experience.setRole(request.getRole());

			if (request.getDescription().isBlank() || request.getDescription().isEmpty())
				experience.setDescription(null);
			else
				experience.setDescription(request.getDescription());

			experience.setStartDate(request.getStart());
			experience.setEndDate(request.getEnd());

			experience.setEmploymentType(employmentTypeRepo.findById(request.getEmploymentId()).orElse(null));
			experienceRepo.save(experience);

			return ResponseEntity.ok().body("Experience added Successfully!");
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	public ResponseEntity<List<ExperienceDTO>> getAllExperience(Integer id) {
		List<ExperienceDTO> experience = experienceRepo.findByJobSeeker_JobSeekerId(id).stream().map(ExperienceDTO::new)
				.toList();
		return ResponseEntity.ok().body(experience);
	}

	public ResponseEntity<String> updateExperience(int id, ExperienceDTO request) {
		try {

			Experience experience = experienceRepo.findById(id).get();

			if (!request.getCompany().isBlank() && !request.getCompany().isEmpty())
				experience.setCompanyName(request.getCompany());

			if (!request.getRole().isBlank() && !request.getRole().isEmpty())
				experience.setRole(request.getRole());

			if (!request.getDescription().isBlank() && !request.getDescription().isEmpty())
				experience.setDescription(request.getDescription());

			if (request.getStart() != null) experience.setStartDate(request.getStart());
			if (request.getEnd() != null) experience.setEndDate(request.getEnd());

			experience.setEmploymentType(employmentTypeRepo.findById(request.getEmploymentId()).orElse(null));
			experienceRepo.save(experience);

			return ResponseEntity.ok().body("Experience updated Successfully!");
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}

	}

	public ResponseEntity<String> deleteExperience(int id) {
		try {
			experienceRepo.deleteById(id);
			return ResponseEntity.ok().body("experience deleted Successfully!");
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
}
