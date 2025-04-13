package com.talenttap.controller;

import java.io.Console;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.talenttap.model.JobseekerRegister;
import com.talenttap.model.Login;
import com.talenttap.service.JobseekerRegisterService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AuthController {

	private JobseekerRegisterService jobseekerRegisterService;
	
	public AuthController(JobseekerRegisterService jobseekerRegisterService){
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
	    return "redirect:/";
	}
}
