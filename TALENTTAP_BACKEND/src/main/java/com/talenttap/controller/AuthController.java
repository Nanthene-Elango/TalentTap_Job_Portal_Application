package com.talenttap.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.talenttap.DTO.AdminRegisterDTO;
import com.talenttap.DTO.EmployerRegisterDTO;
import com.talenttap.DTO.JobseekerRegisterDTO;
import com.talenttap.DTO.LoginDTO;
import com.talenttap.service.AdminService;
import com.talenttap.service.EmployerAuthService;
import com.talenttap.service.JobseekerRegisterService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {

	private JobseekerRegisterService jobseekerRegisterService;
	private EmployerAuthService employerAuthService;
		
    private final AdminService adminService;

	
	public AuthController(JobseekerRegisterService jobseekerRegisterService, EmployerAuthService employerAuthService, AdminService adminService) {
		this.jobseekerRegisterService = jobseekerRegisterService;
		this.employerAuthService = employerAuthService;
	    this.adminService = adminService;
	}
	
	@PostMapping("/register/jobseeker")
	public ResponseEntity<?> registerJobSeeker(@RequestBody JobseekerRegisterDTO request){
		return jobseekerRegisterService.register(request);
	}
	
	@PostMapping("/login/jobseeker")
	public ResponseEntity<?> loginJobSeeker(@RequestBody LoginDTO request , HttpServletResponse response){
		return jobseekerRegisterService.login(request.getUsername() , request.getPassword() , response);
	}
	
	@PostMapping("/register/employer")
    public ResponseEntity<?> registerEmployer(@Valid @ModelAttribute EmployerRegisterDTO dto) {
        try {
            return employerAuthService.register(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }
	
	@PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody AdminRegisterDTO request) {
        return adminService.registerAdmin(request);
    }
	
	 @PostMapping("/login/admin")
	 public ResponseEntity<?> loginAdmin(@Valid @RequestBody LoginDTO request, HttpServletResponse response) {
	     return adminService.loginAdmin(request, response);
    }
}
