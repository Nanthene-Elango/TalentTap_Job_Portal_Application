package com.talenttap.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.talenttap.DTO.EmployerProfileDTO;
import com.talenttap.DTO.JobDisplayDTO;
import com.talenttap.DTO.JobFormDTO;
import com.talenttap.model.Education;
import com.talenttap.model.EducationLevel;
import com.talenttap.model.EmployerRegister;
import com.talenttap.model.IndustryType;
import com.talenttap.model.JobCategory;
import com.talenttap.model.JobFilter;
import com.talenttap.model.Jobs;
import com.talenttap.model.Jobseeker;
import com.talenttap.model.JobseekerRegister;
import com.talenttap.model.JwtToken;
import com.talenttap.model.Location;
import com.talenttap.model.Login;
import com.talenttap.model.Skills;
import com.talenttap.service.EmployerAuthService;
import com.talenttap.service.JobsService;
import com.talenttap.service.JobseekerService;

@Controller
public class PageRenderController {

	private JobseekerService jobseekerService;
	
	private EmployerAuthService employerService;

	private JobsService jobService;
	

	public PageRenderController(JobseekerService jobseekerService, EmployerAuthService employerService,JobsService jobService){
		this.jobseekerService = jobseekerService;
		this.employerService = employerService;
		this.jobService = jobService;
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
	public String LoadJobsPage(Model model) {
		List<Jobs> jobs = jobService.getAllJobs();
		if (jobs != null) {
			model.addAttribute("jobs", jobs);
		}
		List<Location> location = jobseekerService.getAllLocations();
		if (location != null) {
			model.addAttribute("locations" , location);
		}
		List<JobCategory> categories = jobService.getJobCategories();
		if (categories != null) {
			model.addAttribute("categories" , categories);
		}
		model.addAttribute("jobFilter", new JobFilter());
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
	
	@GetMapping("/profile")
	public String LoadProfile(Model model , @CookieValue(value = "jwt", required = false) String jwt) {
		
		if (jwt != null && !jwt.trim().isEmpty()) {
            JwtToken token = new JwtToken(jwt.trim());
            Jobseeker jobseeker = jobseekerService.getJobseeker(token);
    		model.addAttribute("jobSeeker", jobseeker);
    		
    		List<Location> locations = jobseekerService.getAllLocations();
 	        model.addAttribute("locations", locations);
 	        
 	        List<Education> educations = jobseekerService.getAllEducation(jobseeker.getId());
 	        model.addAttribute("educationList", educations);
 	        
 	        List<Skills> skills = jobseekerService.getAllSkillsById(jobseeker.getId());
 	        model.addAttribute("skills", skills);
		}
		
		return "jobseeker/profile";
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

        List<Location> locations = jobseekerService.getAllLocations();
        System.out.println(locations.get(0).getLocation());
        model.addAttribute("locations", locations);

        List<EducationLevel> qualifications = jobseekerService.getEducationLevel();
        model.addAttribute("qualifications", qualifications);

        List<Skills> allSkills = jobseekerService.getAllSkills();
        System.out.println(allSkills.get(0).getSkillId() + " " + allSkills.get(0).getSkill() );
        model.addAttribute("allSkills", allSkills);

        return "jobseeker/register";
    }
	
	@PostMapping("/jobseeker/upload-profile-photo")
    public String uploadProfilePhoto(@RequestParam("profilePhoto") MultipartFile file,
                                     @RequestParam("jobSeekerId") Integer jobSeekerId) {
        	jobseekerService.updateProfilePicture(file , jobSeekerId);
        	return "redirect:/profile";
    }

	@GetMapping("/employer/login")
	public String LoadEmployerLogin(Model model) {
		model.addAttribute("Login",new Login());
		return "employer/login";
	}
	
	@GetMapping("/employer/register")
	public String LoadEmployerResiter(Model model) {
		EmployerRegister register = new EmployerRegister();
		model.addAttribute("employerRegister", register);
		
		List<Location> locations = jobseekerService.getAllLocations();
		System.out.println(locations.get(0).getLocation());
		model.addAttribute("locations",locations);
		
		List<IndustryType> industryType = employerService.getAllIndustryType();
	    System.out.println(industryType.get(0).getIndustryType());
		model.addAttribute("companyIndustry",industryType);
		
		return "employer/register";
	}
	
	@GetMapping("/employer/employerDashboard")
	public String loadEmployerDashboard() {
		return "employer/employerDashboard";
	}
	
	@GetMapping("/employer/jobs")
    public String loadJobs(Model model, @CookieValue(value = "jwt", required = false) String jwt) {
        try {
            if (jwt == null || jwt.trim().isEmpty()) {
                model.addAttribute("error", "Please log in to view jobs.");
                return "error"; // Or redirect to login
            }
            JwtToken token = new JwtToken(jwt.trim());
            List<JobDisplayDTO> jobs = jobService.getAllJobs(token);
            System.out.println(jobs.get(0).getJobType());
            model.addAttribute("jobs", jobs);
            return "employer/jobs";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Invalid JWT token: " + e.getMessage());
            return "error";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Failed to fetch jobs: " + e.getMessage());
            return "error";
        }
    }
	@GetMapping("/employer/candidates")
	public String loadCandidates() {
		return "employer/candidates";
	}
	
	@GetMapping("/employer/profile")
	public String employerProfile(Model model, @CookieValue(value = "jwt", required = false) String jwt) {
	    try {
	        if (jwt != null && !jwt.trim().isEmpty()) {
	            JwtToken token = new JwtToken(jwt.trim());
	            EmployerProfileDTO profile = employerService.profile(token);
	            System.out.println("In employerProfile - Profile fetched: " + (profile != null ? profile.getFullname() : "null"));
	            System.out.println(profile.getCompanyEmail());
	            System.out.println(profile.getPhoneNumber());
	            System.out.println(profile.getEmail());
	            System.out.println(profile.getCompanyLogo());
	            
	            if (profile != null) {
	                model.addAttribute("Fullname", profile.getFullname());
	                model.addAttribute("username", profile.getUsername());
                model.addAttribute("email", profile.getEmail());
                model.addAttribute("phoneNumber", profile.getPhoneNumber());
	                model.addAttribute("companyName", profile.getCompanyName());
	                model.addAttribute("companyIndustry", profile.getIndustryType());
	                model.addAttribute("companyEmail", profile.getCompanyEmail());
	                model.addAttribute("companyPhone", profile.getCompanyPhone());
	                model.addAttribute("designation", profile.getDesignation());
	                model.addAttribute("companyAbout", profile.getAbout());
	                model.addAttribute("companySize", profile.getCompanySize());
	                model.addAttribute("location", profile.getLocation());
	                model.addAttribute("companyLogo", profile.getCompanyLogo());
	                model.addAttribute("loggedIn", true);
	            } 
	        } else {
	            System.out.println("No JWT token found in cookie");
	            model.addAttribute("loggedIn", false);
	        }
	    } catch (Exception e) {
	        System.out.println("Error in employerProfile: " + e.getMessage());
	        model.addAttribute("error", "Unable to load profile data. Please try again.");
	        model.addAttribute("loggedIn", false);
	    }
	    return "employer/employerprofile";
	}
	
	
	@GetMapping("/employer/viewProfile")
	public String loadEmployerProfile() {
		return "employer/candidateProfile";
	}
	
	@GetMapping("/employer/postjob")
	public String loadPostJob(Model model) {
		    JobFormDTO  j = new JobFormDTO();
		    model.addAttribute("jobForm", new JobFormDTO());
	        model.addAttribute("employmentTypes", jobService.getEmploymentType());
	        model.addAttribute("jobCategories", jobService.getJobCategories());
	        model.addAttribute("skills", jobseekerService.getAllSkills());
	        model.addAttribute("locations", jobseekerService.getAllLocations());
		return "employer/postjob";
	}
	
	@GetMapping("/admin/login")
    public String renderAdminLogin(Model model) {
		model.addAttribute("Login",new Login());
        return "admin/admin-login";
    }
	
	@GetMapping("/admin/index")
	public String loadAdminIndex() {
		return "admin/index";
	}
}
