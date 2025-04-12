package com.talenttap.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talenttap.DTO.EducationLevelDTO;
import com.talenttap.DTO.JobseekerRegisterDTO;
import com.talenttap.DTO.LocationDTO;
import com.talenttap.DTO.SkillsDTO;
import com.talenttap.service.EducationService;
import com.talenttap.service.JobseekerRegisterService;
import com.talenttap.service.LocationService;
import com.talenttap.service.SkillService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class JobseekerController {

	private LocationService locationService;
	private SkillService skillService;
	private EducationService educationService;
	private JobseekerRegisterService jobseekerRegisterService;
	
	public JobseekerController(LocationService location , SkillService skillService ,JobseekerRegisterService jobseekerRegisterService, EducationService educationService) {
		this.locationService = location;
		this.skillService = skillService;
		this.educationService = educationService;
		this.jobseekerRegisterService = jobseekerRegisterService;
	}
	
	@GetMapping("/locations")
	public ResponseEntity<List<LocationDTO>> getAllLocations(){
		return ResponseEntity.ok().body(locationService.getAllLocations());
	}
	
	@GetMapping("/skills")
	public ResponseEntity<List<SkillsDTO>> getAllSkills(){
		return ResponseEntity.ok().body(skillService.getAllSkills());
	}
	
	@GetMapping("/educationlevel")
	public ResponseEntity<List<EducationLevelDTO>> getAllEducationLevel(){
		return ResponseEntity.ok().body(educationService.getAllEducationLevel());
	}
	
	@PostMapping("/auth/register/jobseeker")
	public ResponseEntity<?> registerJobSeeker(@RequestBody JobseekerRegisterDTO request){
		return jobseekerRegisterService.register(request);
	}
	
}
