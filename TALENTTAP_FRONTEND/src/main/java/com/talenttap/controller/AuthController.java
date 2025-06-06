package com.talenttap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.talenttap.model.JobseekerRegister;
import com.talenttap.model.Login;
import com.talenttap.service.JobseekerService;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AuthController {

	private JobseekerService jobseekerRegisterService;
	
	public AuthController(JobseekerService jobseekerRegisterService){
		this.jobseekerRegisterService = jobseekerRegisterService;
	}
	
	@PostMapping("/register")
    public String processRegistration(@ModelAttribute("jobseekerRegister") JobseekerRegister jobseekerRegister) {
        
    	if (jobseekerRegister.getDegree().equals("")) {
    		jobseekerRegister.setDegree(null);
    	}
    	if (jobseekerRegister.getBoardOfStudy().equals("")) {
    		jobseekerRegister.setBoardOfStudy(null);
    	}
        System.out.println("fullname: " + jobseekerRegister.getFullName());
        System.out.println("skills: " + jobseekerRegister.getSkillIds());
        System.out.println("location: " + jobseekerRegister.getLocation());
        System.out.println("Board of study: " + jobseekerRegister.getBoardOfStudy());
        System.out.println("Degree: " + jobseekerRegister.getDegree());
        System.out.println("Email: " + jobseekerRegister.getEmail());
        System.out.println("Institution: " + jobseekerRegister.getInstitution());
        System.out.println("Password: " + jobseekerRegister.getPassword());
        System.out.println("Username: " + jobseekerRegister.getUsername());
        System.out.println("Endyear: " + jobseekerRegister.getEndYear());
        System.out.println("startyear: " + jobseekerRegister.getStartYear());
        System.out.println("phonenumber: " + jobseekerRegister.getPhoneNumber());
        System.out.println("percentage: " + jobseekerRegister.getPercentage());
        System.out.println("year of experience: " + jobseekerRegister.getYearsOfExperience());
        System.out.println("highestQualification: " + jobseekerRegister.getHighestQualification());
        
        jobseekerRegisterService.register(jobseekerRegister);
        return "redirect:/login";
    }
	
	@PostMapping("/login")
	public String loginUser(@ModelAttribute Login login, HttpServletResponse response) {
	    jobseekerRegisterService.login(login , response);
	    System.out.println("in login user method");
	    return "redirect:/";
	}
	

	
	@GetMapping("/logout")
	public String logout(HttpServletResponse response) {
	    Cookie cookie = new Cookie("jwt", null);
	    cookie.setPath("/");             
	    cookie.setHttpOnly(true);       
	    cookie.setMaxAge(0);            
	    response.addCookie(cookie);

	    return "redirect:/";     
	}
}
