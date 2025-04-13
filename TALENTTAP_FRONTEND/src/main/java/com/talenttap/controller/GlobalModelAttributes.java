package com.talenttap.controller;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.talenttap.model.JwtToken;
import com.talenttap.service.JobseekerService;

@Component
@ControllerAdvice
public class GlobalModelAttributes {

	private JobseekerService jobseekerService;
	
	public GlobalModelAttributes(JobseekerService jobseekerService) {
		this.jobseekerService = jobseekerService;
	}
	
    @ModelAttribute
    public void addGlobalAttributes(Model model, @CookieValue(value = "jwt", required = false) String jwt) {
    	
    	try {
            if (jwt != null && !jwt.trim().isEmpty()) {
                JwtToken token = new JwtToken(jwt.trim());
                String fullName = jobseekerService.getFullName(token);
                model.addAttribute("fullname", fullName);
                model.addAttribute("loggedIn", true);
            } else {
                model.addAttribute("fullname", "");
                model.addAttribute("loggedIn", false);
            }
        } catch (Exception e) {
            System.out.println("Error in JWT handling: " + e.getMessage());
            model.addAttribute("fullname", "");
            model.addAttribute("loggedIn", false);
        }
    }
}
