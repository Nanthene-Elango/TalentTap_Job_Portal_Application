package com.talenttap.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.talenttap.DTO.EmployerProfileDTO;
import com.talenttap.DTO.EmployerRegisterDTO;
import com.talenttap.model.EmployerRegister;
import com.talenttap.model.JwtToken;
import com.talenttap.model.Login;
import com.talenttap.service.EmployerAuthService;
import com.talenttap.service.JobseekerService;

import java.util.stream.Collectors;

@Controller
public class EmployerAuthController {

    private EmployerAuthService employerService;
    
    private JobseekerService jobseekerService;

    public EmployerAuthController(EmployerAuthService employerService, JobseekerService jobseekerService) {
        this.employerService = employerService;
        this.jobseekerService = jobseekerService;
    }

    @PostMapping("/employerRegister")
    public String processEmployerRegistration(
            @Valid @ModelAttribute("employerRegister") EmployerRegister employerRegister,
            BindingResult bindingResult,
            @RequestParam("companyLogo") MultipartFile companyLogo,
            Model model) {
        // Log form data for debugging
        System.out.println("Form Data - phoneNumber: " + employerRegister.getPhoneNumber());
        System.out.println("Form Data - emailId: " + employerRegister.getEmailId());
        System.out.println("Form Data - webUrl: " + employerRegister.getWebUrl());

        // Check for validation errors
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            model.addAttribute("error", "Please correct the following: " + errorMessage);
            model.addAttribute("companyIndustry", employerService.getAllIndustryType());
            return "employer/register";
        }

        try {
            // Validate companyLogo
            if (companyLogo.isEmpty()) {
                model.addAttribute("error", "Company logo is required.");
                model.addAttribute("companyIndustry", employerService.getAllIndustryType());
                return "employer/register";
            }

            // Validate file size (e.g., max 5MB)
            if (companyLogo.getSize() > 5 * 1024 * 1024) {
                model.addAttribute("error", "Company logo size must not exceed 5MB.");
               
                model.addAttribute("companyIndustry", employerService.getAllIndustryType());
                return "employer/register";
            }

            // Map to DTO
            EmployerRegisterDTO dto = new EmployerRegisterDTO();
            dto.setFullName(employerRegister.getFullName());
            dto.setUsername(employerRegister.getUsername());
            dto.setPassword(employerRegister.getPassword());
            dto.setPhoneNumber(employerRegister.getPhoneNumber());
            dto.setEmailId(employerRegister.getEmailId());
            dto.setCompanyName(employerRegister.getCompanyName());
            dto.setCompanyLogo(companyLogo);
            dto.setIndustryType(employerRegister.getIndustryType());
            dto.setCompanyEmail(employerRegister.getCompanyEmail());
            dto.setCompanyPhoneNumber(employerRegister.getCompanyPhoneNumber());
            dto.setCompanySize(employerRegister.getCompanySize());
            dto.setLocationId(employerRegister.getLocationId());
            dto.setWebUrl(employerRegister.getWebUrl());
            dto.setFoundedAt(employerRegister.getFoundedAt());
            dto.setAbout(employerRegister.getAbout());
            dto.setDesignation(employerRegister.getDesignation());

            // Log DTO for debugging
            System.out.println("DTO - phoneNumber: " + dto.getPhoneNumber());
            System.out.println("DTO - emailId: " + dto.getEmailId());
            System.out.println("DTO - webUrl: " + dto.getWebUrl());

            employerService.register(dto);
            return "redirect:/employer/login";
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            String errorMessage = e.getMessage().contains("400") ?
                    "Invalid data submitted: Please ensure phone number, email, and website URL are valid." :
                    "Registration failed: " + e.getMessage();
            model.addAttribute("error", errorMessage);
          
            return "employer/register";
        }
    }
    
    @PostMapping("/employer/Login")
    public String loginUser(@ModelAttribute("Login") Login login, BindingResult result, HttpServletResponse response, Model model) {
        boolean success = employerService.login(login, response, model);
        if (!success) {
            model.addAttribute("Login", login);
            return "employer/login";
        }
        return "redirect:/employer/employerDashboard";
    }
    
	@GetMapping("/employer/Logout")
	public String logout(HttpServletResponse response) {
	    Cookie cookie = new Cookie("jwt", null);
	    cookie.setPath("/");             
	    cookie.setHttpOnly(true);       
	    cookie.setMaxAge(0);            
	    response.addCookie(cookie);
	    return "redirect:/";     
	}
	

	
	
	
}