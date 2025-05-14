package com.talenttap.controller;

import java.util.List;
import java.util.Map;

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
	
	public JobsController(JobService jobService, JobseekerRegisterService jobSeekerService) {
		this.jobService = jobService;
		this.jobseekerService = jobSeekerService;
	}
	
	// Get all Job types available to display in the dropdown in frontend
	@GetMapping("/jobtype")
	public ResponseEntity<List<EmploymentTypeDTO>> getAllIndustryType(){
		return ResponseEntity.ok().body(jobService.getAllEmploymentType());
	}
	
	// Get all Job types available to display in the dropdown in frontend
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
    
    // get job by id
    @GetMapping("api/job/{id}")
	public ResponseEntity<Jobs> getJobById(@PathVariable int id) {
		return ResponseEntity.ok(jobService.getJobById(id));
	}
    
    @PatchMapping("api/job/{id}/toggleStatus")
    public ResponseEntity<Jobs> toggleJobStatus(@PathVariable int id){
    	return ResponseEntity.ok(jobService.changeJobStatus(id));
    }
    
   @DeleteMapping("{id}/delete")
   public ResponseEntity<Void> deleteJob(@PathVariable int id){
	   boolean deleted = jobService.deleteJob(id);
	   
	   if(!deleted) {
		   return ResponseEntity.notFound().build();
	   }
	   else {
		   return ResponseEntity.noContent().build();
		   }
   }
   
   @PutMapping("job/edit")
   public ResponseEntity<Jobs> editJob(@RequestBody EditJob job){
	   return ResponseEntity.ok(jobService.editJob(job));
   }
   
   @GetMapping("/search")
   public ResponseEntity<Page<Jobs>> searchJobs(
           @RequestParam String keyword,
           @RequestParam(defaultValue = "0") int page,
           @RequestParam(defaultValue = "10") int size,
           @RequestParam(defaultValue = "postedDate") String sortBy,
           @RequestParam(defaultValue = "desc") String direction) {

       PageRequest pageable = PageRequest.of(
           page,
           size,
           direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending()
       );

       Page<Jobs> result = jobService.searchJobs(keyword, pageable);
       return ResponseEntity.ok(result);
   }
	
}
