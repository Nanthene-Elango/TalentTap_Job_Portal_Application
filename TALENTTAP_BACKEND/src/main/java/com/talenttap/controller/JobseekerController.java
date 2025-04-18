package com.talenttap.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talenttap.DTO.EducationLevelDTO;
import com.talenttap.DTO.JobseekerDTO;
import com.talenttap.DTO.JobseekerRegisterDTO;
import com.talenttap.DTO.LocationDTO;
import com.talenttap.DTO.SkillsDTO;
import com.talenttap.service.EducationService;
import com.talenttap.service.JobseekerRegisterService;
import com.talenttap.service.LocationService;
import com.talenttap.service.SkillService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin("*")
public class JobseekerController {

	private LocationService locationService;
	private SkillService skillService;
	private EducationService educationService;
	private JobseekerRegisterService jobseekerService;
	
	public JobseekerController(LocationService location , SkillService skillService, 
			EducationService educationService , JobseekerRegisterService jobseekerService) {
		this.locationService = location;
		this.skillService = skillService;
		this.educationService = educationService;
		this.jobseekerService = jobseekerService;
	}
	
	@GetMapping("api/locations")
	public ResponseEntity<List<LocationDTO>> getAllLocations(){
		return ResponseEntity.ok().body(locationService.getAllLocations());
	}
	
	@GetMapping("api/skills")
	public ResponseEntity<List<SkillsDTO>> getAllSkills(){
		return ResponseEntity.ok().body(skillService.getAllSkills());
	}
	
	@GetMapping("api/educationlevel")
	public ResponseEntity<List<EducationLevelDTO>> getAllEducationLevel(){
		return ResponseEntity.ok().body(educationService.getAllEducationLevel());
	}
	
	@PostMapping("jobseeker/fullName")
	public ResponseEntity<String> getFullName(@RequestBody Map<String,String> jwt){
		return jobseekerService.getFullName(jwt.get("jwt"));
	}
	
	@PostMapping("jobseeker")
	public ResponseEntity<JobseekerDTO> getJobseeker(@RequestBody Map<String,String> jwt){
		return jobseekerService.getJobseeker(jwt.get("jwt"));
	}

	@GetMapping("jobseeker/profile-photo/{id}")
	public ResponseEntity<?> getProfilePhoto(@PathVariable Integer id) {
		return jobseekerService.getProfilePhotoById(id);
	}
}
