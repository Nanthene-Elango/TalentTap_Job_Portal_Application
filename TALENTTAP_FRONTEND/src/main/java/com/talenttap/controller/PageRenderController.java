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
	
    //	employer controller
	@GetMapping("/employer/login")
	public String LoadEmployerLogin() {
		return "employer/login";
	}
	
	@GetMapping("/employer/register")
	public String LoadEmployerResiter() {
		return "employer/register";
	}
	
	@GetMapping("/employer/employerDashboard")
	public String loadEmployerDashboard() {
		return "employer/employerDashboard";
	}
	
	@GetMapping("/employer/jobs")
	public String loadJobs(){
		return "employer/jobs";
	}
	
	@GetMapping("/employer/candidates")
	public String loadCandidates() {
		return "employer/candidates"; // goes to static/template/employer/candidates
	}
	
	@GetMapping("/employer/profile")
	public String loademployerProfile() {
		return "employer/employerProfile";
	}
	
	@GetMapping("/employer/viewProfile")
	public String loadEmployerProfile() {
		return "employer/candidateProfile";
	}
	
	@GetMapping("/employer/postjob")
	public String loadPostJob() {
		return "employer/postjob";
	}
}
