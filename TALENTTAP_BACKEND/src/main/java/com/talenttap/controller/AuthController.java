package com.talenttap.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.talenttap.DTO.JobseekerRegisterDTO;
import com.talenttap.DTO.LoginDTO;
import com.talenttap.service.JobseekerRegisterService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/auth")
public class AuthController {

	private JobseekerRegisterService jobseekerRegisterService;
	
	public AuthController(JobseekerRegisterService jobseekerRegisterService) {
		this.jobseekerRegisterService = jobseekerRegisterService;
	}
	
	@PostMapping("/register/jobseeker")
	public ResponseEntity<?> registerJobSeeker(@RequestBody JobseekerRegisterDTO request){
		return jobseekerRegisterService.register(request);
	}
	
	@PostMapping("/login/jobseeker")
	public ResponseEntity<?> loginJobSeeker(@RequestBody LoginDTO request , HttpServletResponse response){
		return jobseekerRegisterService.login(request.getUsername() , request.getPassword() , response);
	}
}
