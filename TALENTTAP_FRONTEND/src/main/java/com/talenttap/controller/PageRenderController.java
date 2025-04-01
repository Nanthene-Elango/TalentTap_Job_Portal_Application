package com.talenttap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageRenderController {

	@GetMapping
	public String LoadHomePage() {
		return "index";
	}

	@GetMapping("/aboutus")
	public String LoadAboutUsPage() {
		return "jobseeker/aboutus";
	}
	
	@GetMapping("/jobs")
	public String LoadJobsPage() {
		return "jobseeker/jobs";
	}

	@GetMapping("/companies")
	public String LoadCompaniesPage() {
		return "jobseeker/companies";
	}
	
	@GetMapping("/companyProfile")
	public String LoadCompanyProfile() {
		return "jobseeker/companyProfile";
	}
	
	@GetMapping("/login")
	public String LoadJobseekerLogin() {
		return "jobseeker/login";
	}
	
	@GetMapping("/register")
	public String LoadJobseekerRegister() {
		return "jobseeker/register";
	}
	
	@GetMapping("/employer/login")
	public String LoadEmployerLogin() {
		return "employer/login";
	}
}
