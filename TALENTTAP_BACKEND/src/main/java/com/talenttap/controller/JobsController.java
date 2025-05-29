package com.talenttap.controller;

import java.util.List;
import java.util.Map;
import com.talenttap.DTO.EmployerJobFilterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.talenttap.DTO.EditJob;
import com.talenttap.DTO.EmploymentTypeDTO;
import com.talenttap.DTO.IndustryTypeDTO;
import com.talenttap.DTO.JobCategoryDTO;
import com.talenttap.DTO.JobDTO;
import com.talenttap.DTO.JobDisplayDTO;
import com.talenttap.DTO.JobFormDTO;
import com.talenttap.DTO.JobType;
import com.talenttap.entity.Jobs;
import com.talenttap.service.JobService;
import com.talenttap.service.JobseekerRegisterService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/jobs")
public class JobsController {

	private JobService jobService;

	private JobseekerRegisterService jobseekerService;

	private static final Logger logger = LoggerFactory.getLogger(JobsController.class);

	public JobsController(JobService jobService, JobseekerRegisterService jobSeekerService) {
		this.jobService = jobService;
		this.jobseekerService = jobSeekerService;
	}

	// Get all Job types available to display in the dropdown in frontend
	@GetMapping("/jobtype")
	public ResponseEntity<List<EmploymentTypeDTO>> getAllIndustryType() {
		return ResponseEntity.ok().body(jobService.getAllEmploymentType());
	}

	// Get all Job types available to display in the dropdown in frontend
	@GetMapping("/jobCategory")
	public ResponseEntity<List<JobCategoryDTO>> getAllJobCategory() {
		return ResponseEntity.ok().body(jobService.getAllJobCategory());
	}

	@PostMapping("/post-job")
	public ResponseEntity<String> postJob(@RequestBody JobFormDTO jobFormDTO,
			@RequestHeader("Authorization") String authorization) {
		try {
			// Extract JWT from "Bearer <token>"
			String jwt = authorization.replace("Bearer ", "").trim();

			// Save job to database
			String savedJob = jobService.saveJob(jobFormDTO, jwt);

			// Return success response
			return ResponseEntity.ok("Job posted successfully with ID:");
		} catch (Exception e) {
			// Log error and return failure response
			e.printStackTrace();
			return ResponseEntity.status(500).body("Error posting job: " + e.getMessage());
		}
	}

	@PostMapping("/dashboard/jobs")
	public ResponseEntity<List<JobDisplayDTO>> getDashboardJobs(@RequestHeader("Authorization") String authHeader) {
		try {
			String token = authHeader.replace("Bearer ", "");
			List<JobDisplayDTO> jobs = jobService.getTop2ActiveJobs(token);
			return ResponseEntity.ok(jobs);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(null);
		} catch (RuntimeException e) {
			return ResponseEntity.status(404).body(null);
		}
	}

	// get job by id
	@GetMapping("api/job/{id}")
	public ResponseEntity<Jobs> getJobById(@PathVariable int id) {
		return ResponseEntity.ok(jobService.getJobById(id));
	}

//	@PatchMapping("api/job/{id}/toggleStatus")
//	public ResponseEntity<Jobs> toggleJobStatus(@PathVariable int id) {
//		return ResponseEntity.ok(jobService.changeJobStatus(id));
//	}
	
	@PutMapping("api/job/{id}/toggleStatus")
	public ResponseEntity<String> toggleJobStatus(@PathVariable int id) {
	    Jobs updatedJob = jobService.changeJobStatus(id);

	    if (updatedJob == null) {
	        return ResponseEntity.notFound().build();
	    }
	    return ResponseEntity.ok(updatedJob.getJobStatus().name());
	}


	@DeleteMapping("{id}/delete")
	public ResponseEntity<Void> deleteJob(@PathVariable int id) {
		boolean deleted = jobService.deleteJob(id);

		if (!deleted) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.noContent().build();
		}
	}

	@PutMapping("job/edit")
	public ResponseEntity<String> editJob(@RequestBody EditJob job) {
		return ResponseEntity.ok(jobService.editJob(job));
	}

	@PostMapping("/searchAndFilter")  // Changed to POST because you have a @RequestBody
	public ResponseEntity<List<JobDisplayDTO>> searchJobs(@RequestBody EmployerJobFilterDTO dto) {
	    List<JobDisplayDTO> result = jobService.searchJobs(dto);
	    return ResponseEntity.ok(result);
	}

	@GetMapping("/getAllJobs")
	public ResponseEntity<List<JobDisplayDTO>> getEmployerJobs(@RequestHeader("Authorization") String authHeader) {
		
		String token = authHeader.replace("Bearer ", "");
		logger.info("Received request to fetch jobs with token: {}",
				 token.hashCode());

		List<JobDisplayDTO> jobs = jobService.getAllJobs(token);
		logger.info("Returning {} job(s)", jobs.size());

		return ResponseEntity.ok(jobs);
	}
	
	@GetMapping("/getAllExpiredJobs")
	public ResponseEntity<List<JobDisplayDTO>> getEmployerExpiredJobs(@RequestHeader("Authorization") String authHeader) {
		
		String token = authHeader.replace("Bearer ", "");
		logger.info("Received request to fetch jobs with token: {}",
				 token.hashCode());

		List<JobDisplayDTO> jobs = jobService.getAllExpiredJobs(token);
		logger.info("Returning {} job(s)", jobs.size());

		return ResponseEntity.ok(jobs);
	}

}
