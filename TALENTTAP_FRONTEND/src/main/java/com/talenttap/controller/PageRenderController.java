package com.talenttap.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.talenttap.model.EducationLevel;
import com.talenttap.model.JobseekerRegister;
import com.talenttap.model.Location;
import com.talenttap.model.Login;
import com.talenttap.model.Skills;
import com.talenttap.service.JobseekerService;

@Controller
public class PageRenderController {

	private JobseekerService jobseekerRegisterService;
	
	public PageRenderController(JobseekerService jobseekerRegisterService){
		this.jobseekerRegisterService = jobseekerRegisterService;
	}
	
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
	public String LoadJobseekerLogin(Model model) {
		model.addAttribute("Login", new Login()); 
		return "jobseeker/login";
	}
	
	@GetMapping("/register")
    public String showRegistrationForm(Model model) {
        JobseekerRegister jobseekerRegister = new JobseekerRegister();
        model.addAttribute("jobseekerRegister", jobseekerRegister);

        List<Location> locations = jobseekerRegisterService.getAllLocations();
        System.out.println(locations.get(0).getLocation());
        model.addAttribute("locations", locations);

        List<EducationLevel> qualifications = jobseekerRegisterService.getEducationLevel();
        model.addAttribute("qualifications", qualifications);

        List<Skills> allSkills = jobseekerRegisterService.getAllSkills();
        System.out.println(allSkills.get(0).getSkillId() + " " + allSkills.get(0).getSkill() );
        model.addAttribute("allSkills", allSkills);

        return "jobseeker/register";
    }

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
		return "employer/candidates";
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
