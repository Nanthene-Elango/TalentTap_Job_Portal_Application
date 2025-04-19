package com.talenttap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.talenttap.model.Jobseeker;
import com.talenttap.service.JobseekerService;

@Controller
public class JobseekerController {

	private JobseekerService jobseekerService;
	
	public JobseekerController(JobseekerService jobseekerService) {
		this.jobseekerService = jobseekerService;
	}
	
	@PostMapping("/saveJobSeeker")
	public String updateJobseekerProfile(@ModelAttribute Jobseeker jobSeeker) {
		jobseekerService.updateProfile(jobSeeker);
		return "redirect:/profile";
	}
	
	@PostMapping("jobseeker/update-summary/{id}")
	public String updateProfileSummary(@RequestParam("summary") String summary , @PathVariable int id) {
		jobseekerService.updateSummary(summary , id);
		return "redirect:/profile";
	}
	
	
}
