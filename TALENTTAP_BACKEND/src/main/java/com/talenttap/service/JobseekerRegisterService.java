package com.talenttap.service;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
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

import com.talenttap.DTO.EducationDTO;
import com.talenttap.DTO.JobDTO;
import com.talenttap.DTO.JobFilterDTO;
import com.talenttap.DTO.JobseekerDTO;
import com.talenttap.DTO.JobseekerRegisterDTO;
import com.talenttap.DTO.SkillsDTO;
import com.talenttap.entity.ApplicationStatus;
import com.talenttap.entity.AuthProvider;
import com.talenttap.entity.Education;
import com.talenttap.entity.Gender;
import com.talenttap.entity.JobApplication;
import com.talenttap.entity.Users;
import com.talenttap.exceptions.InvalidCredentialsException;
import com.talenttap.entity.JobSeeker;
import com.talenttap.entity.Jobs;
import com.talenttap.entity.Skills;
import com.talenttap.repository.EducationLevelRepository;
import com.talenttap.repository.EducationRepository;
import com.talenttap.repository.JobApplicationRepository;
import com.talenttap.repository.JobsRepository;
import com.talenttap.repository.JobseekerRepository;
import com.talenttap.repository.LocationRepository;
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
	private SkillsRepository skillsRepo;
	private RoleRepository roleRepo;
	private LocationRepository locationRepo;
	private EducationLevelRepository educationLevelRepo;
	private PasswordEncoder passwordEncoder;
	private JobsRepository jobRepo;
	private JobApplicationRepository jobApplicationRepo;

	public JobseekerRegisterService(UsersRepository userRepo, JobseekerRepository jobseekerRepo,
			EducationRepository educationRepo, SkillsRepository skillsRepo, RoleRepository roleRepo,
			LocationRepository locationRepo, EducationLevelRepository educationLevelRepo,
			PasswordEncoder passwordEncoder, JobsRepository jobRepo, JobApplicationRepository jobApplicationRepo) {
		this.educationRepo = educationRepo;
		this.jobseekerRepo = jobseekerRepo;
		this.skillsRepo = skillsRepo;
		this.userRepo = userRepo;
		this.locationRepo = locationRepo;
		this.roleRepo = roleRepo;
		this.educationLevelRepo = educationLevelRepo;
		this.passwordEncoder = passwordEncoder;
		this.jobRepo = jobRepo;
		this.jobApplicationRepo = jobApplicationRepo;
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

		Users user = userRepo.findByUsername(username).get();

		if (user != null && passwordEncoder.matches(password, user.getPassword())) {
			String jwt = jwtUtil.generateToken(username, "JOBSEEKER");

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
			application.setDateOfApplication(LocalDate.now());
			application.setJob(job);
			application.setJobSeeker(jobseeker);
			application.setLastUpdated(LocalDate.now());
			application.setStatus(ApplicationStatus.pending);

			jobApplicationRepo.save(application);
			
			return ResponseEntity.ok().body("Job Applied Successfully!");
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}

	public ResponseEntity<Boolean> hasApplied(String jwt, int jobId) {
		
		boolean hasApplied = false;
		if (jwt != null && !jwt.trim().isEmpty()) {

			String username = jwtUtil.extractIdentifier(jwt);
			Users user = userRepo.findByUsername(username).get();

			JobSeeker jobseeker = jobseekerRepo.findByUser(user).get();

			if (jobApplicationRepo.existsByJobSeekerAndJob_JobId(jobseeker , jobId)) {
				hasApplied = true;
			}
			
		}
		
		return ResponseEntity.ok(hasApplied);
	}
}
