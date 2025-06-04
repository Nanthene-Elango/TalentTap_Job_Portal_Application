package com.talenttap.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talenttap.DTO.CompanyProfileDTO;
import com.talenttap.service.CompanyService;

@RestController
@RequestMapping("/api/employer/company")
public class CompanyController {
	
	private CompanyService companyService;
	
	public CompanyController(CompanyService companyService) {
		this.companyService = companyService;
	}
	
	@GetMapping("/profile")
	public ResponseEntity<CompanyProfileDTO> getCompanyProfile(@RequestHeader(value = "Authorization", required = false) String token){
		return ResponseEntity.ok(companyService.getCompanyProfile(token));
	}
	
	@PutMapping("/update")
	public ResponseEntity<String> updateCompanyProfile(@RequestHeader(value = "Authorization", required = false) String token, @RequestBody CompanyProfileDTO profile){
		return ResponseEntity.ok(companyService.updateCompanyProfile(token, profile));
	}
	
	@GetMapping("/logo")
	public ResponseEntity<String> getCompanyLogo(@RequestHeader(value = "Authorization", required = false) String token){
		return ResponseEntity.ok(companyService.getCompanyLogo(token));
	}
	
	@PutMapping("/logo/update")
	public ResponseEntity<String> getCompanyLogoUpdate(@RequestHeader(value = "Authorization", required = false) String token, @RequestBody byte[] logo){
		return ResponseEntity.ok(companyService.updateCompanyLogo(token, logo));
	}
}
