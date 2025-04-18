package com.talenttap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.talenttap.DTO.JobFormDTO;
import com.talenttap.model.JwtToken;
import com.talenttap.service.EmployerAuthService;
import com.talenttap.service.JobsService;
import com.talenttap.service.JobseekerService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/employer")
public class JobsController {

    private final JobseekerService jobseekerService;
    private final JobsService jobService;
    private final EmployerAuthService employerService;

    public JobsController(JobsService jobService, EmployerAuthService employerService, JobseekerService jobseekerService) {
        this.jobseekerService = jobseekerService;
        this.jobService = jobService;
        this.employerService = employerService;
    }

    @PostMapping("/post-job")
    public String postJob(@Valid @ModelAttribute("jobForm") JobFormDTO jobFormDTO, BindingResult result, Model model,
            @CookieValue(value = "jwt", required = false) String jwt, HttpServletRequest request) {
        // Print all JobFormDTO fields for debugging
        System.out.println("--- JobFormDTO Values ---");
        System.out.println("Job Role: " + jobFormDTO.getJobRole());
        System.out.println("Job Type ID: " + jobFormDTO.getJobTypeId());
        System.out.println("Job Category ID: " + jobFormDTO.getJobCategoryId());
        System.out.println("Job Description: " + jobFormDTO.getJobDescription());
        System.out.println("Skill IDs: " + jobFormDTO.getSkillIds());
        System.out.println("Responsibilities: " + jobFormDTO.getResponsibilities());
        System.out.println("Requirements: " + jobFormDTO.getRequirements());
        System.out.println("Benefits: " + jobFormDTO.getBenefits());
        System.out.println("Work Type: " + jobFormDTO.getWorkType());
        System.out.println("Location IDs: " + jobFormDTO.getLocationIds());
        System.out.println("Years of Experience: " + jobFormDTO.getYearsOfExperience());
        System.out.println("Salary Min: " + jobFormDTO.getSalaryMin());
        System.out.println("Salary Max: " + jobFormDTO.getSalaryMax());
        System.out.println("Duration: " + jobFormDTO.getDuration());
        System.out.println("Stipend: " + jobFormDTO.getStipend());
        System.out.println("Openings: " + jobFormDTO.getOpenings());
        System.out.println("Deadline: " + jobFormDTO.getDeadline());
        System.out.println("Cookies received: " + request.getHeader("Cookie"));

        if (jwt != null && !jwt.trim().isEmpty()) {
            if (result.hasErrors()) {
                model.addAttribute("employmentTypes", jobService.getEmploymentType());
                model.addAttribute("jobCategories", jobService.getJobCategories());
                model.addAttribute("skills", jobseekerService.getAllSkills());
                model.addAttribute("locations", jobseekerService.getAllLocations());
                System.out.println("Validation errors: " + result.getAllErrors());
                return "employer/postjob";
            }

            try {
                ResponseEntity<String> response = jobService.addJob(jobFormDTO, jwt);
                if (response.getStatusCode().is2xxSuccessful()) {
                    System.out.println("Job posted successfully: " + response.getBody());
                    return "redirect:/employer/jobs";
                } else {
                    System.out.println("Failed to post job: " + response.getStatusCode() + " - " + response.getBody());
                    model.addAttribute("error", "Failed to post job. Please try again.");
                    return "employer/postjob";
                }
            } catch (Exception e) {
                System.out.println("Error posting job: " + e.getMessage());
                model.addAttribute("error", "An error occurred while posting the job.");
                return "employer/postjob";
            }
        } else {
            System.out.println("No valid JWT token found");
            model.addAttribute("error", "Authentication required. Please log in.");
            return "employer/postjob";
        }
    }
    
}