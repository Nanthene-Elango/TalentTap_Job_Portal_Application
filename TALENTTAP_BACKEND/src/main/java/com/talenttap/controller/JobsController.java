package com.talenttap.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talenttap.DTO.EmploymentTypeDTO;
import com.talenttap.DTO.IndustryTypeDTO;
import com.talenttap.DTO.JobCategoryDTO;
import com.talenttap.DTO.JobDisplayDTO;
import com.talenttap.DTO.JobFormDTO;
import com.talenttap.DTO.JobType;
import com.talenttap.entity.Jobs;
import com.talenttap.service.JobService;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/jobs")
public class JobsController {
	
	private JobService jobService;
	
	public JobsController(JobService jobService) {
		this.jobService = jobService;
	}
	
	@GetMapping("/jobtype")
	public ResponseEntity<List<EmploymentTypeDTO>> getAllIndustryType(){
		return ResponseEntity.ok().body(jobService.getAllEmploymentType());
		}
  
	@GetMapping("/jobCategory")
	public ResponseEntity<List<JobCategoryDTO>> getAllJobCategory(){
		return ResponseEntity.ok().body(jobService.getAllJobCategory());
	}
	
	@PostMapping("/post-job")
    public ResponseEntity<String> postJob(@RequestBody JobFormDTO jobFormDTO, @RequestHeader("Authorization") String authorization) {
        try {
            // Extract JWT from "Bearer <token>"
            String jwt = authorization.replace("Bearer ", "").trim();

            // Save job to database
            String savedJob = jobService.saveJob(jobFormDTO,jwt);

            // Return success response
            return ResponseEntity.ok("Job posted successfully with ID:");
        } catch (Exception e) {
            // Log error and return failure response
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error posting job: " + e.getMessage());
        }
    }
	
	@PostMapping("/jobs")
    public ResponseEntity<List<JobDisplayDTO>> getEmployerJobs(@RequestBody Map<String,String> jwt) {
        try {
            List<JobDisplayDTO> jobs = jobService.getAllJobs(jwt.get("jwt"));
            return ResponseEntity.ok(jobs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping("/dashboard/jobs")
    public ResponseEntity<List<JobDisplayDTO>> getDashboardJobs(@RequestBody Map<String,String> jwt) {
        try {
            List<JobDisplayDTO> jobs = jobService.getTop2ActiveJobs(jwt.get("jwt"));
            return ResponseEntity.ok(jobs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        }
    }
	
	
}
