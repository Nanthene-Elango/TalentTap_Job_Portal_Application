package com.talenttap.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.talenttap.DTO.EmployerProfileDTO;
import com.talenttap.DTO.CompanyUpdateDTO;
import com.talenttap.DTO.IndustryTypeDTO;
import com.talenttap.DTO.LoginDTO;
import com.talenttap.service.EmployerAuthService;
import com.talenttap.service.EmployerService;
import com.talenttap.service.IndustryTypeService;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class EmployerController {
	
	private IndustryTypeService industryTypeService;
	
	private EmployerAuthService employerAuthService;
	
	private EmployerService employerService;
	
	public EmployerController( IndustryTypeService industryTypeService, EmployerAuthService employerAuthService, EmployerService employerService) {
		this.industryTypeService = industryTypeService;	
		this.employerAuthService = employerAuthService;
		this.employerService = employerService;
	}
	
	@GetMapping("/industry")
	public ResponseEntity<List<IndustryTypeDTO>> getAllIndustryType(){
		return ResponseEntity.ok().body(industryTypeService.getAllIndustryType());
		}
	
	@PostMapping("/employer/profile")
	public ResponseEntity<?> getProfile(@RequestBody Map<String,String> jwt){
		EmployerProfileDTO d = employerAuthService.profile(jwt.get("jwt"));
		System.out.println("username from service"+ d.getUsername());
		return ResponseEntity.ok().body(d);
	}
	
	// update employer profile photo
	@PutMapping("employer/update-profile-photo")
	public ResponseEntity<String> updateCompanyProfilePhoto(@RequestParam("profilePhoto") MultipartFile file, 
			@RequestParam("employerId") Integer id){
		return employerService.updateCompanyProfilePhoto(file,id);
	}
	
	// update employer profile data
	@PutMapping("employer/profile")
	public ResponseEntity<String> updateCompanyProfile(@RequestBody CompanyUpdateDTO dto, 
			@RequestHeader("Authorization") String authHeader) throws Exception {
	    String token = authHeader.replace("Bearer ", "");
		
		return employerService.updateCompanyProfile(dto, token);
	}
	
	// verify employer
	@PostMapping("employer/verify")
	public ResponseEntity<String> verifyEmployer(@RequestBody LoginDTO verifyEmployer,
			@RequestHeader("Authorization") String authHeader){
		String token = authHeader.replace("Bearer ", "");
		return ResponseEntity.ok(employerService.verifyEmployer(verifyEmployer, token));
	}
}
