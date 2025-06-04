package com.talenttap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.talenttap.DTO.EditJobFormDTO;
import com.talenttap.DTO.JobDisplayDTO;
import com.talenttap.DTO.JobFormDTO;
import com.talenttap.DTO.JobUpdateFormDTO;
import com.talenttap.model.EmployerJobFilter;
import com.talenttap.model.JobCategory;
import com.talenttap.model.Jobs;
import com.talenttap.model.JwtToken;
import com.talenttap.model.Location;
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
    
    
    // edit job
    @PostMapping("/editjob")
    public String editJob(@Valid @ModelAttribute("jobForm") EditJobFormDTO jobFormDTO, BindingResult result, Model model,
            @CookieValue(value = "jwt", required = false) String jwt, HttpServletRequest request) {
        // Print all JobFormDTO fields for debugging
        System.out.println("--- JobFormDTO Values ---");
        System.out.println("job id"+ jobFormDTO.getJobId());
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
                return "error";
            }

            try {
                ResponseEntity<String> response = jobService.updateJob(jobFormDTO, jwt);
                if (response.getStatusCode().is2xxSuccessful()) {
                    System.out.println("Job posted successfully: " + response.getBody());
                    return "redirect:/employer/jobs";
                } else {
                    System.out.println("Failed to post job: " + response.getStatusCode() + " - " + response.getBody());
                    model.addAttribute("error", "Failed to post job. Please try again.");
                    return "error";
                }
            } catch (Exception e) {
                System.out.println("Error posting job: " + e.getMessage());
                model.addAttribute("error", "An error occurred while posting the job.");
                return "error";
            }
        } else {
            System.out.println("No valid JWT token found");
            model.addAttribute("error", "Authentication required. Please log in.");
            return "error";
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    @PostMapping("/job/{id}/toggle")
    public String toggleJobStatus(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
        	System.out.println("Reaching the controller");
            String updatedStatus = jobService.toggleJobStatus(id);  // returns "Open" or "Closed"
            redirectAttributes.addFlashAttribute("message", "Status changed successfully!");
            redirectAttributes.addFlashAttribute("updatedJobStatus", updatedStatus);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to change status: " + e.getMessage());
        }
        return "redirect:/employer/jobs";  // make sure this loads job list with correct model
    }
    
    @PostMapping("/job/{id}/delete")
    public String deleteJob(@PathVariable String id,
                            RedirectAttributes redirectAttributes) {
        // You can use deleteReason and deleteOtherReason for logging or validation
        int ids = Integer.parseInt(id);
        boolean deleted = jobService.deleteJob(ids);
        if (deleted) {
            redirectAttributes.addFlashAttribute("message", "Job deleted successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "Job not found or deletion failed");
        }
        return "redirect:/employer/jobs";
    }
    
    @PostMapping("/filterEmployerJobs")
	public String filterJobs(@ModelAttribute EmployerJobFilter jobFilter, Model model) {
		System.out.println("Employment type: " + jobFilter.getEmploymentType());
		System.out.println("Job status: " + jobFilter.getJobStatus());
		System.out.println("Search keyword: " + jobFilter.getKeyword());
		System.out.println("location id: " + jobFilter.getLocation());
		System.out.println("worktype: " + jobFilter.getWorkType());
 
		// Set location to 0 if null or empty
        if (jobFilter.getLocation() == null || jobFilter.getLocation().toString().isEmpty()) {
            jobFilter.setLocation(0);
        }	
        if (jobFilter.getEmploymentType() == null || jobFilter.getEmploymentType().toString().isEmpty()) {
            jobFilter.setEmploymentType(0);
        }
        
        if (jobFilter.getKeyword() == null || jobFilter.getKeyword().toString().isEmpty()) {
            jobFilter.setKeyword("");
        }
        if (jobFilter.getJobStatus() == null || jobFilter.getJobStatus().toString().isEmpty()) {
            jobFilter.setJobStatus("");
        }
        if (jobFilter.getWorkType() == null || jobFilter.getWorkType().toString().isEmpty()) {
            jobFilter.setWorkType("");
        }
        
        System.out.println("Employment type: " + jobFilter.getEmploymentType());
		System.out.println("Job status: " + jobFilter.getJobStatus());
		System.out.println("Search keyword: " + jobFilter.getKeyword());
		System.out.println("location id: " + jobFilter.getLocation());
		System.out.println("worktype: " + jobFilter.getWorkType());
        System.out.println("location id: " + jobFilter.getLocation());
        System.out.println("Employment type: " + jobFilter.getLocation());
    	model.addAttribute("currentPage", "jobs");
        List<JobDisplayDTO> jobs = jobService.filterJobs(jobFilter);
     

		if (jobs != null) {
			System.out.println("Hoo");
			if (jobs != null && !jobs.isEmpty()) {
			    System.out.println("First Job: " + jobs.get(0));
			}

			model.addAttribute("jobs", jobs);
			model.addAttribute("jobFilter", jobFilter); //
			List<Location> location = jobseekerService.getAllLocations();
			if (location != null) {
				model.addAttribute("locations", location);
			}
			List<JobCategory> categories = jobService.getJobCategories();
			if (categories != null) {
				model.addAttribute("categories", categories);
			}
			model.addAttribute("employmentTypes", jobService.getEmploymentType());	
			return "employer/jobs";
		} else {
			return "error";
		}
	}


    

 
    
}