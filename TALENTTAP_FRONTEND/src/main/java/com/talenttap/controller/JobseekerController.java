package com.talenttap.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	public String getJobDetail(@PathVariable int id, Model model) {
		Jobs job = jobseekerService.getJobById(id);
		model.addAttribute("job", job);
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

	@PostMapping("/profile/delete-skill/{id}")
	public String deleteSkill(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		jobseekerService.deleteSkillById(id);
		redirectAttributes.addFlashAttribute("message", "Skill deleted successfully!");
		return "redirect:/profile";
	}
}
