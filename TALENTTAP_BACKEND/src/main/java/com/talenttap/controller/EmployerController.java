package com.talenttap.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.talenttap.DTO.EmployerProfileDTO;
import com.talenttap.DTO.CandidatesDTO;
import com.talenttap.DTO.CompanyUpdateDTO;
import com.talenttap.DTO.DashboardMetrics;
import com.talenttap.DTO.EmailDTO;
import com.talenttap.DTO.IndustryTypeDTO;
import com.talenttap.DTO.LoginDTO;
import com.talenttap.entity.Jobs;
import com.talenttap.entity.JobApplication;
import com.talenttap.service.EmployerAuthService;
import com.talenttap.service.EmployerService;
import com.talenttap.service.IndustryTypeService;
import com.talenttap.service.JobService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class EmployerController {
	
	private IndustryTypeService industryTypeService;
	
	private EmployerAuthService employerAuthService;
	
	private EmployerService employerService;
	
	private JobService jobService;
	
	public EmployerController( IndustryTypeService industryTypeService, EmployerAuthService employerAuthService, EmployerService employerService, JobService jobService) {
		this.industryTypeService = industryTypeService;	
		this.employerAuthService = employerAuthService;
		this.employerService = employerService;
		this.jobService = jobService;
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
	
	
	
	// Employer Login
	@PostMapping("auth/login/employer")
	public ResponseEntity<Map<String, Object>> loginEmployer(@Valid @RequestBody LoginDTO loginRequest , HttpServletResponse response){
		return ResponseEntity.ok(employerAuthService.login(loginRequest.getUsername() , loginRequest.getPassword() , response));
	}
	
	
	@GetMapping("/employer/candidates")
	public ResponseEntity<List<CandidatesDTO>> getAllAppliedCandidates(@RequestHeader("Authorization") String authHeader){
		String token = authHeader.replace("Bearer ", "");
		return ResponseEntity.ok(jobService.getAllAppliedCandidates(token));
	}
	
	
	@GetMapping("/employer/candidates/recent")
	public ResponseEntity<List<CandidatesDTO>> getRecentAppliedCandidates(@RequestHeader("Authorization") String authHeader){
		String token = authHeader.replace("Bearer ", "");
		return ResponseEntity.ok(jobService.getRecentAppliedCandidates(token));
	}
	
	@PostMapping("api/candidate/{id}/approve")
	public ResponseEntity<Void> approveCandidate(@RequestHeader("Authorization") String authHeader, @RequestBody EmailDTO email,@PathVariable int id) {
		String token = authHeader.replace("Bearer ", "");
		JobApplication updatedJob = employerService.approveApplication(id, token);

	    if (updatedJob == null) {
	        return ResponseEntity.notFound().build();
	    }
	    return ResponseEntity.ok().build();
	}

	@PatchMapping("api/candidate/{id}/reject")
	public ResponseEntity<Void> rejectCandidate(@RequestHeader("Authorization") String authHeader, @RequestBody EmailDTO email,@PathVariable int id) {
		String token = authHeader.replace("Bearer ", "");
		JobApplication updatedJob = employerService.rejectApplication(id, token);

	    if (updatedJob == null) {
	        return ResponseEntity.notFound().build();
	    }
	    return ResponseEntity.ok().build();
	}
	
	@GetMapping("api/dashboard/metrics")
	public ResponseEntity<DashboardMetrics> getAllDashBoard(@RequestHeader("Authorization") String authHeader){
		String token = authHeader.replace("Bearer ", "");
		return ResponseEntity.ok(employerService.getAllDashboardMetrics(token));
	}
	
	@PostMapping("/verify/employer/company")
	public ResponseEntity<Boolean> verifyEmployerAndCompany(@RequestHeader("Authorization") String authHeader){
		String token = authHeader.replace("Bearer ", "");
		return ResponseEntity.ok(employerService.verifyEmployerAndCompany(token));
	}
	
	@GetMapping("/isVerified")
	public ResponseEntity<Boolean> isVerified(@RequestHeader("Authorization") String authHeader){
		String token = authHeader.replace("Bearer ", "");
	    return  ResponseEntity.ok(employerService.isCompanyVerified(token));
	}
	
	
	// verify employer
		@PostMapping("employer/verify")
		public ResponseEntity<String> verifyEmployer(@RequestBody Map<String,String> password,
				@RequestHeader("Authorization") String authHeader){
			String token = authHeader.replace("Bearer ", "");
			return ResponseEntity.ok(employerService.verifyEmployer(password.get("password"), token));
		}
		
	
	
	
}
