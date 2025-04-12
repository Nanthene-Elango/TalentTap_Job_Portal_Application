package com.talenttap.service;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.talenttap.DTO.JobseekerRegisterDTO;
import com.talenttap.entity.AuthProvider;
import com.talenttap.entity.Education;
import com.talenttap.entity.Users;
import com.talenttap.entity.JobSeeker;
import com.talenttap.repository.EducationLevelRepository;
import com.talenttap.repository.EducationRepository;
import com.talenttap.repository.JobseekerRepository;
import com.talenttap.repository.LocationRepository;
import com.talenttap.repository.RoleRepository;
import com.talenttap.repository.SkillsRepository;
import com.talenttap.repository.UsersRepository;

import jakarta.validation.Valid;

@Service
public class JobseekerRegisterService {

	private UsersRepository userRepo;
	private JobseekerRepository jobseekerRepo;
	private EducationRepository educationRepo;
	private SkillsRepository skillsRepo;
	private RoleRepository roleRepo;
	private LocationRepository locationRepo;
	private EducationLevelRepository educationLevelRepo;
	
	
	public JobseekerRegisterService(UsersRepository userRepo , JobseekerRepository jobseekerRepo , EducationRepository educationRepo , SkillsRepository skillsRepo , RoleRepository roleRepo , LocationRepository locationRepo , EducationLevelRepository educationLevelRepo) {
		this.educationRepo = educationRepo;
		this.jobseekerRepo = jobseekerRepo;
		this.skillsRepo = skillsRepo;
		this.userRepo = userRepo;
		this.locationRepo = locationRepo;
		this.roleRepo = roleRepo;
		this.educationLevelRepo = educationLevelRepo;
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
		user.setPassword(request.getPassword());
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

}
