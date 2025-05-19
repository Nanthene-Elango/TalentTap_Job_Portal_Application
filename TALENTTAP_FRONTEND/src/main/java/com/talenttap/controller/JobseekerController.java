package com.talenttap.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.talenttap.DTO.EducationDTO;
import com.talenttap.model.Education;
import com.talenttap.model.JobCategory;
import com.talenttap.model.JobFilter;
import com.talenttap.model.Jobs;
import com.talenttap.model.Jobseeker;
import com.talenttap.model.Location;
import com.talenttap.service.JobsService;
import com.talenttap.service.JobseekerService;

@Controller
public class JobseekerController {

	private JobseekerService jobseekerService;
	private JobsService jobService;

	public JobseekerController(JobseekerService jobseekerService, JobsService jobService) {
		this.jobseekerService = jobseekerService;
		this.jobService = jobService;
	}

	@PostMapping("/saveJobSeeker")
	public String updateJobseekerProfile(@ModelAttribute Jobseeker jobSeeker) {
		jobseekerService.updateProfile(jobSeeker);
		return "redirect:/profile";
	}

	@PostMapping("jobseeker/update-summary/{id}")
	public String updateProfileSummary(@RequestParam("summary") String summary, @PathVariable int id) {
		jobseekerService.updateSummary(summary, id);
		return "redirect:/profile";
	}

	@GetMapping("job/{id}/detail")
	public String getJobDetail(@PathVariable int id,@CookieValue(value = "jwt", required = false) String jwt, Model model) {
		Jobs job = jobseekerService.getJobById(id);
		model.addAttribute("job", job);
		
		boolean isApplied = false;
	    if (jwt != null && !jwt.isBlank()) {
	        isApplied = jobseekerService.hasApplied(jwt, id);
	    }

	    model.addAttribute("isApplied", isApplied);
	    
		return "jobseeker/jobDetail";
	}

	@PostMapping("/filterJobs")
	public String filterJobs(@ModelAttribute JobFilter jobFilter, Model model) {
		System.out.println("Experience: " + jobFilter.getExperience());
		System.out.println("Job type: " + jobFilter.getJobType());
		System.out.println("Salary: " + jobFilter.getSalary());
		System.out.println("duration: " + jobFilter.getDuration());
		System.out.println("Freshness: " + jobFilter.getFreshness());
		System.out.println("Location: " + jobFilter.getLocation());
		System.out.println("Industry: " + jobFilter.getCategory());
		System.out.println("stipend: " + jobFilter.getStipend());

		List<Jobs> jobs = jobseekerService.filterJobs(jobFilter);

		if (jobs != null) {
			model.addAttribute("jobs", jobs);
			List<Location> location = jobseekerService.getAllLocations();
			if (location != null) {
				model.addAttribute("locations", location);
			}
			List<JobCategory> categories = jobService.getJobCategories();
			if (categories != null) {
				model.addAttribute("categories", categories);
			}
			return "jobseeker/jobs";
		} else {
			return "redirect:/jobs";
		}
	}
	
	@PostMapping("/jobseeker/skill/add/{id}")
	public String addMultipleSkillsToJobSeeker(@PathVariable("id") Long jobSeekerId,
	                                           @RequestParam("skillIds") List<Long> skillIds,
	                                           RedirectAttributes redirectAttributes) {

	    return jobseekerService.addSkills(jobSeekerId , skillIds , redirectAttributes);
	}

	@PostMapping("/profile/delete-skill/{id}/{jobseekerId}")
	public String deleteSkill(@PathVariable Long id, @PathVariable Long jobseekerId ,  RedirectAttributes redirectAttributes) {
		jobseekerService.deleteSkillById(id , jobseekerId);
		redirectAttributes.addFlashAttribute("message", "Skill deleted successfully!");
		return "redirect:/profile";
	}
	
	@PostMapping("/resume/upload")
    public String handleResumeUpload(@RequestParam("file") MultipartFile file,
                                     @CookieValue(value = "jwt", required = false) String jwt,
                                     RedirectAttributes redirectAttributes) {
		return jobseekerService.handleResumeUpload(file, jwt, redirectAttributes);
	}
	
	@GetMapping("/downloadResume")
	public ResponseEntity<byte[]> downloadResume(@CookieValue(value = "jwt", required = false) String jwt) {
	   return jobseekerService.downloadResume(jwt);
	}
	
	@GetMapping("/previewResume")
	public ResponseEntity<byte[]> previewResumeViaRestTemplate(@CookieValue(value = "jwt", required = false) String jwt) {
	    return jobseekerService.previewResume(jwt);
	}
	
	@PostMapping("/deleteResume")
	public String removeResume(@CookieValue(value = "jwt", required = false) String jwt, RedirectAttributes redirectAttributes) {
	   return jobseekerService.deleteResume(jwt , redirectAttributes);
	}
	
	@PostMapping("/job-application/apply/{id}")
	public String applyJob(@CookieValue(value = "jwt", required = false) String jwt,
	                       @PathVariable int id,
	                       RedirectAttributes redirectAttributes) {

	    jobseekerService.applyJob(jwt, id);

	    redirectAttributes.addFlashAttribute("success", "Job applied successfully.");
	    return "redirect:/jobs";
	}
	
	@PostMapping("jobseeker/education/add/{id}")
	public String addEducation(@PathVariable int id , @ModelAttribute EducationDTO education , RedirectAttributes redirectAttributes) {
		return jobseekerService.addEducation(id , education , redirectAttributes);
	}
	
	@PostMapping("/jobseeker/education/edit")
	public String updateEducation(@ModelAttribute Education education, RedirectAttributes redirectAttributes) {
		return jobseekerService.editEducation(education , redirectAttributes);
	}
	
	@PostMapping("/jobseeker/education/delete/{id}")
	public String deleteEducationById(@PathVariable int id , RedirectAttributes redirectAttributes) {
		return jobseekerService.deleteEducation(id , redirectAttributes);
	}
}
