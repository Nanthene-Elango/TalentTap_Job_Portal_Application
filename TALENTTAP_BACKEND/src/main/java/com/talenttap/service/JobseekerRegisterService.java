package com.talenttap.service;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.talenttap.DTO.JobseekerDTO;
import com.talenttap.DTO.JobseekerRegisterDTO;

import com.talenttap.entity.AuthProvider;
import com.talenttap.entity.Education;
import com.talenttap.entity.Gender;
import com.talenttap.entity.Users;
import com.talenttap.exceptions.InvalidCredentialsException;
import com.talenttap.entity.JobSeeker;
import com.talenttap.repository.EducationLevelRepository;
import com.talenttap.repository.EducationRepository;
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
	
	
	public JobseekerRegisterService(UsersRepository userRepo , JobseekerRepository jobseekerRepo ,
			EducationRepository educationRepo , SkillsRepository skillsRepo , 
			RoleRepository roleRepo , LocationRepository locationRepo , 
			EducationLevelRepository educationLevelRepo , PasswordEncoder passwordEncoder) {
		this.educationRepo = educationRepo;
		this.jobseekerRepo = jobseekerRepo;
		this.skillsRepo = skillsRepo;
		this.userRepo = userRepo;
		this.locationRepo = locationRepo;
		this.roleRepo = roleRepo;
		this.educationLevelRepo = educationLevelRepo;
		this.passwordEncoder = passwordEncoder;
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

	public ResponseEntity<?> login(String username , String password , HttpServletResponse response) {
		
		Users user = userRepo.findByUsername(username).get();
		
		if (user != null && passwordEncoder.matches(password , user.getPassword())) {
			String jwt = jwtUtil.generateToken(username, "JOBSEEKER");

			System.out.println(jwt);
	        Cookie cookie = new Cookie("jwt", jwt);
	        cookie.setHttpOnly(true);
	        cookie.setPath("/");
	        cookie.setSecure(false);
	        cookie.setMaxAge(24 * 60 * 60);
	        response.addCookie(cookie);
	        
	        return ResponseEntity.ok().body("User Logged in Successfully");
		}
		else {
			throw new InvalidCredentialsException("Invalid Username/Password!");
		}
	}

	public ResponseEntity<String> getFullName(String jwt) {
		
		System.out.println(jwt);
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

	    if (imageBytes[0] == (byte)0xFF && imageBytes[1] == (byte)0xD8) {
	        return "image/jpeg";
	    } else if (imageBytes[0] == (byte)0x89 && imageBytes[1] == (byte)0x50) {
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
			if (request.getPassword() != null && !request.getPassword().isBlank() && !request.getPassword().isBlank()) {
				user.setPassword(request.getPassword());
			}
			user = userRepo.save(user);
			
			jobseeker.setUser(user);
			jobseeker.setDob(request.getDob());
			jobseeker.setLocation(locationRepo.findByLocation(request.getLocation()).get());

			if(request.getGender() != null && !request.getGender().isBlank() && !request.getGender().isEmpty()) {
				Gender gender = Gender.valueOf(request.getGender().toUpperCase());
				jobseeker.setGender(gender);
				
			}
			
			jobseekerRepo.save(jobseeker);
			
			return ResponseEntity.ok("Updated Succsessfully!");
		}
		catch(Exception ex) {
			return ResponseEntity.badRequest().body("failed to update profile!");
		}
		
	
	}

	public ResponseEntity<String> updateSummary(String summary, int id) {
		JobSeeker jobseeker = jobseekerRepo.findById(id).get();
		
		jobseeker.setProfileSummary(summary);
		jobseekerRepo.save(jobseeker);
		
		return ResponseEntity.ok().body("Updated summary successfully!");
		
	}

}
