package com.talenttap.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talenttap.DTO.AdminJobDTO;
import com.talenttap.service.JobService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	private JobService jobService;
	
	public AdminController(JobService jobService) {
		this.jobService = jobService;
	}
	
    @GetMapping("/verify")
    public ResponseEntity<?> verifyAdmin() {
        return ResponseEntity.ok("Authorized");
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok("Logged out successfully");
    }
    

	// get all jobs
	@GetMapping("/jobs")
	public ResponseEntity<List<AdminJobDTO>> getAllJobs(@RequestBody Map<String,String> jwt){
		return  ResponseEntity.ok(jobService.getAllJobsAdmin(jwt.get("jwt")));
	
	}

}