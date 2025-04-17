package com.talenttap.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talenttap.DTO.EmployerProfileDTO;
import com.talenttap.DTO.IndustryTypeDTO;
import com.talenttap.service.EmployerAuthService;
import com.talenttap.service.IndustryTypeService;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class EmployerController {
	
	private IndustryTypeService industryTypeService;
	
	private EmployerAuthService employerService;
	
	public EmployerController( IndustryTypeService industryTypeService, EmployerAuthService employerService) {
		this.industryTypeService = industryTypeService;	
		this.employerService = employerService;
	}
	
	@GetMapping("/industry")
	public ResponseEntity<List<IndustryTypeDTO>> getAllIndustryType(){
		return ResponseEntity.ok().body(industryTypeService.getAllIndustryType());
		}
	
	@PostMapping("/employer/profile")
	public ResponseEntity<?> getProfile(@RequestBody Map<String,String> jwt){
		EmployerProfileDTO d = employerService.profile(jwt.get("jwt"));
		System.out.println("username from service"+ d.getUsername());
		return ResponseEntity.ok().body(d);
	}
}
