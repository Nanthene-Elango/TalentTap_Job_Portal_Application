package com.talenttap.controller;

import java.util.ArrayList;

import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.talenttap.DTO.CandidatesDTO;
import com.talenttap.DTO.EditJobFormDTO;
import com.talenttap.DTO.EmployerProfileDTO;
import com.talenttap.DTO.JobDisplayDTO;
import com.talenttap.DTO.JobFormDTO;
import com.talenttap.model.Education;
import com.talenttap.model.EducationLevel;
import com.talenttap.model.EmployerRegister;
import com.talenttap.model.EmploymentType;
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
			model.addAttribute("locations", location);
		}
		List<JobCategory> categories = jobService.getJobCategories();
		if (categories != null) {
			model.addAttribute("categories", categories);
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
	public String LoadProfile(Model model, @CookieValue(value = "jwt", required = false) String jwt) {

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
		System.out.println(allSkills.get(0).getSkillId() + " " + allSkills.get(0).getSkill());
		model.addAttribute("allSkills", allSkills);

		return "jobseeker/register";
	}

	@PostMapping("/jobseeker/upload-profile-photo")
	public String uploadProfilePhoto(@RequestParam("profilePhoto") MultipartFile file,
			@RequestParam("jobSeekerId") Integer jobSeekerId) {
		jobseekerService.updateProfilePicture(file, jobSeekerId);
		return "redirect:/profile";
	}

	@PostMapping("/applyJob/{jobId}")
	public String showApplicationPage(@PathVariable Long jobId , @CookieValue(value = "jwt", required = false) String jwt) {
	    if (jwt == null) return "redirect:/login";

	    else return "redirect:/job/application/" + jobId;
	}

	@GetMapping("/job/application/{jobId}")
	public String renderApplication(@PathVariable("jobId") int jobId ,
			@CookieValue(value = "jwt", required = false) String jwt,
            Model model) {
	
			JwtToken token = new JwtToken(jwt.trim());
		    Jobseeker jobseeker = jobseekerService.getJobseeker(token);
		    Jobs job = jobseekerService.getJobById(jobId);

		    model.addAttribute("job", job);
		    model.addAttribute("jobSeeker", jobseeker);

		    byte[] resume = jobseeker.getResume();
		    if (resume != null) {
		        String base64 = Base64.getEncoder().encodeToString(resume);
		        model.addAttribute("resumePresent", true);
		        model.addAttribute("resumeBase64", base64);
		    } else {
		        model.addAttribute("resumePresent", false);
		    }
		
		    return "jobseeker/jobApplication";
	}
	
	@GetMapping("/employer/login")
	public String LoadEmployerLogin(Model model) {
		model.addAttribute("Login", new Login());
		return "employer/login";
	}

	@GetMapping("/employer/register")
	public String LoadEmployerResiter(Model model) {
		EmployerRegister register = new EmployerRegister();
		model.addAttribute("employerRegister", register);

		List<Location> locations = jobseekerService.getAllLocations();
		System.out.println(locations.get(0).getLocation());
		model.addAttribute("locations", locations);

		List<IndustryType> industryType = employerService.getAllIndustryType();
		System.out.println(industryType.get(0).getIndustryType());
		model.addAttribute("companyIndustry", industryType);

		return "employer/register";
	}

	@GetMapping("/employer/employerDashboard")
	public String loadEmployerDashboard(Model model, @CookieValue(value = "jwt", required = false) JwtToken jwt) throws Exception {
		model.addAttribute("currentPage", "dashboard");
		List<CandidatesDTO> candidates= jobService.getAllAppliedCandidates(jwt);
		List<JobDisplayDTO> getTop2ActiveJobs = jobService.getAllDashBoardJobs(jwt);
		model.addAttribute("applicants", candidates);
		model.addAttribute("jobs",getTop2ActiveJobs);
		return "employer/employerDashboard";
	}

	@GetMapping("/employer/jobs")
    public String loadJobs(Model model, @CookieValue(value = "jwt", required = false) String jwt) {
		System.out.println("reaching the controller hii");
		model.addAttribute("currentPage", "jobs");
        try {
            if (jwt == null || jwt.trim().isEmpty()) {
                model.addAttribute("error", "Please log in to view jobs.");
                return "employer/jobs"; // Or redirect to login
            }
            JwtToken token = new JwtToken(jwt.trim());
            List<JobDisplayDTO> jobs = jobService.getAllJobs(token);
           System.out.println("hi");
           System.out.println(jobs.get(0).getJobId());
            System.out.println(jobs.get(0).getJobDescription());
            if (jobs == null || jobs.isEmpty()) {
                model.addAttribute("jobs", new ArrayList<>()); // ensures not null
            } else {
                model.addAttribute("jobs", jobs);
            }
            
         // 🔥 Add a blank form DTO for editing
            EditJobFormDTO job = new EditJobFormDTO();
            model.addAttribute("jobForm", job);
	        model.addAttribute("employmentTypes", jobService.getEmploymentType());
	        model.addAttribute("jobCategories", jobService.getJobCategories());
	        model.addAttribute("skills", jobseekerService.getAllSkills());
	        model.addAttribute("locations", jobseekerService.getAllLocations());
	       
            return "employer/jobs";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Invalid JWT token: " + e.getMessage());
            return "error";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Failed to fetch jobs: " + e.getMessage());
            return "error";
        }
    }
	
	@GetMapping("/employer/jobs/update/{id}")
	public String editJobs(@PathVariable("id") Long id,
	                       Model model,
	                       @CookieValue(value = "jwt", required = false) String jwt) {
	    System.out.println(id);
	    try {
	        if (jwt == null || jwt.trim().isEmpty()) {
	            model.addAttribute("error", "Please log in to view jobs.");
	            return "employer/jobs";
	        }

	        JwtToken token = new JwtToken(jwt.trim());

	        
	        // ✅ Get the job with the specific ID
	        JobDisplayDTO selectedJob = jobService.getAllJobs(token)
	            .stream()
	            .filter(j -> j.getJobId() ==  id)
	            .findFirst()
	            .orElse(null);

	        if (selectedJob == null) {
	            model.addAttribute("error", "Job not found.");
	            return "error";
	        }
	        List<EmploymentType> employmentTypes = jobService.getEmploymentType();
	        String jobTypeName = selectedJob.getJobType(); // e.g., "Part Time"
	        Integer jobTypeId = employmentTypes.stream()
	                .filter(et -> et.getEmploymentType().equalsIgnoreCase(jobTypeName))
	                .map(EmploymentType::getEmploymentTypeId)
	                .findFirst()
	                .orElse(null);
	        // ✅ Populate EditJobFormDTO from selected job
	        
	        List<JobCategory> jobCategory = jobService.getJobCategories();

	        String jobTypeCategory = selectedJob.getJobCategory();
	        System.out.println("jobtypecategory"+jobTypeCategory);// e.g., "Part Time"
	        Integer jobCategoryId = jobCategory.stream()
	                .filter(et -> et.getJobCategory().equalsIgnoreCase(jobTypeCategory))
	                .map(JobCategory::getJobCategoryId)
	                .findFirst()
	                .orElse(null);
	        
	        // get all skills
	        List<Skills> allSkills = jobseekerService.getAllSkills(); 
	     // From selected job
	        Set<String> selectedSkillNames = selectedJob.getRequiredSkills(); // e.g., ["Java", "SQL"]

	   

	        // Map names to IDs
	        List<Integer> selectedSkillIds = allSkills.stream()
	            .filter(skill -> selectedSkillNames.contains(skill.getSkill()))
	            .map(Skills::getSkillId)
	            .collect(Collectors.toList());
	        System.out.println(selectedSkillIds);

	        // Set in form
	        
// Each Skill has id and name
	        
	        // locations
	        List<Location> allLocations = jobseekerService.getAllLocations(); 
	     // Each Location has locationId and locationName
	        Set<String> selectedLocationNames = selectedJob.getLocations(); 
	     // e.g., ["Chennai", "Bangalore"]

	        List<Integer> selectedLocationIds = allLocations.stream()
	        	    .filter(loc -> selectedLocationNames.contains(loc.getLocation()))
	        	    .map(Location::getLocationId)
	        	    .collect(Collectors.toList());
	        

	        
	        

	        System.out.println(id);
	       
	        
	        EditJobFormDTO jobForm = new EditJobFormDTO();
	        jobForm.setJobId(id);
	        jobForm.setJobRole(selectedJob.getJobRole());
	        jobForm.setJobDescription(selectedJob.getJobDescription());
	        System.out.println(selectedJob.getDeadline().toLocalDate());
	        jobForm.setJobTypeId(jobTypeId); // assuming this field exists
	        jobForm.setJobCategoryId(jobCategoryId);
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        String formattedDate = selectedJob.getDeadline().format(formatter);
	        jobForm.setWorkType(selectedJob.getWorkType());
	        jobForm.setBenefits(selectedJob.getBenefits());
	        jobForm.setDeadline(formattedDate);
	        jobForm.setOpenings(selectedJob.getOpenings());
	        jobForm.setStipend(selectedJob.getStipend());
	        jobForm.setDuration(selectedJob.getDuration());
	        jobForm.setSalaryMax(selectedJob.getSalaryMax());
	        jobForm.setSkillIds(selectedSkillIds);
	        jobForm.setLocationIds(selectedLocationIds);  // Make sure this field exists in EditJobFormDTO
	        jobForm.setSalaryMin(selectedJob.getSalaryMin());
	        jobForm.setYearsOfExperience(selectedJob.getYearsOfExperience());
	        jobForm.setRequirements(selectedJob.getRoles());
	        jobForm.setResponsibilities(selectedJob.getResponsibilities());
	        System.out.println(selectedJob.getLocations());
	        
	        
	        // Debug logging
	        System.out.println("Current Locale: " + Locale.getDefault());
	        System.out.println("Selected Job Deadline: " + selectedJob.getDeadline());
	        System.out.println("Rendering jobForm.deadline: " + jobForm.getDeadline());
	        System.out.println("Deadline String: " + jobForm.getDeadline().toString() + "|"); // Check for spaces
	        System.out.println("selectedJob.duration: " + selectedJob.getDuration());
	        System.out.println("jobForm.duration: " + jobForm.getDuration());
	        System.out.println("selectedJob.jobType: " + selectedJob.getJobType());
	        System.out.println("jobForm.jobTypeId: " + jobForm.getJobTypeId());
	        System.out.println("employmentTypes: " + employmentTypes);
	        
	        // ... Set other fields as needed

	        // ✅ Add required attributes
	        model.addAttribute("employmentTypes", jobService.getEmploymentType());
	        model.addAttribute("jobCategories", jobService.getJobCategories());
	        model.addAttribute("skills", jobseekerService.getAllSkills());
	        model.addAttribute("locations", jobseekerService.getAllLocations());
	        model.addAttribute("jobForm", jobForm);

	        return "employer/editjob";

	    } catch (Exception e) {
	        model.addAttribute("error", "Failed to fetch job data: " + e.getMessage());
	        return "error";
	    }
	}

	
	
	
	@GetMapping("/employer/candidates")
	public String loadCandidates(Model model,  @CookieValue(value = "jwt", required = false) JwtToken jwt) throws Exception {
		model.addAttribute("currentPage","candidates");
		List<CandidatesDTO> candidates= jobService.getAllAppliedCandidates(jwt);
		model.addAttribute("applicants", candidates);
		return "employer/candidates";
	}

	@GetMapping("/employer/profile")
	public String employerProfile(Model model, @CookieValue(value = "jwt", required = false) String jwt) {
		try {
			if (jwt != null && !jwt.trim().isEmpty()) {
				JwtToken token = new JwtToken(jwt.trim());
				EmployerProfileDTO profile = employerService.profile(token);
				System.out.println(
						"In employerProfile - Profile fetched: " + (profile != null ? profile.getFullname() : "null"));
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
		    model.addAttribute("jobForm", new JobFormDTO());
	        model.addAttribute("employmentTypes", jobService.getEmploymentType());
	        model.addAttribute("jobCategories", jobService.getJobCategories());
	        model.addAttribute("skills", jobseekerService.getAllSkills());
	        model.addAttribute("locations", jobseekerService.getAllLocations());
		return "employer/postjob";
	}
	
	// edit job
	@GetMapping("/employer/postjob/edit/{jobId}")
	public String updateJob(@PathVariable int jobId, 
	                       @CookieValue(value = "jwt", required = false) String jwt, 
	                       Model model) {
	    try {
	        if (jwt == null || jwt.trim().isEmpty()) {
	            model.addAttribute("error", "Please log in to update jobs.");
	            return "error";
	        }
	        JwtToken token = new JwtToken(jwt.trim());
	        
	        // Fetch job details by jobId
	        JobFormDTO jobForm = jobService.getJobById(jobId, token);
	        if (jobForm == null) {
	            model.addAttribute("error", "Job not found.");
	            return "error";
	        }
	        
	        // Add jobForm and other required attributes to the model
	        model.addAttribute("jobForm", jobForm);
	        model.addAttribute("employmentTypes", jobService.getEmploymentType());
	        model.addAttribute("jobCategories", jobService.getJobCategories());
	        model.addAttribute("skills", jobseekerService.getAllSkills());
	        model.addAttribute("locations", jobseekerService.getAllLocations());
	        
	        return "employer/editjob"; // Render the edit job page
	    } catch (IllegalArgumentException e) {
	        model.addAttribute("error", "Invalid JWT token: " + e.getMessage());
	        return "error";
	    } catch (RuntimeException e) {
	        model.addAttribute("error", "Failed to fetch job: " + e.getMessage());
	        return "error";
	    }
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
