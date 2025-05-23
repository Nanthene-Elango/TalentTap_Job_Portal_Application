package com.talenttap.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talenttap.DTO.AdminJobDTO;
import com.talenttap.service.JobService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	private JobService jobService;
	
	public AdminController(JobService jobService) {
		this.jobService = jobService;
	}
	
    @GetMapping("/verify")
    public ResponseEntity<?> verifyAdmin() {
        return ResponseEntity.ok("Authorized");
    }


	// get all jobs
    @GetMapping("/jobs")
    public ResponseEntity<List<AdminJobDTO>> getAllJobs(@RequestHeader("Authorization") String jwtToken) {
        try {
            String jwt = jwtToken.replace("Bearer ", "");
            System.out.println("JWT Token received: " + jwtToken);
            return ResponseEntity.ok(jobService.getAllJobsAdmin(jwt));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
    @GetMapping("/jobs/{id}")
    public ResponseEntity<AdminJobDTO> getJobById(@PathVariable("id") Integer jobId,
                                                  @RequestHeader("Authorization") String jwtToken) {
        try {
            String jwt = jwtToken.replace("Bearer ", "");
            AdminJobDTO job = jobService.getJobById(jobId, jwt);
            if (job == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(job);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
	
	@PostMapping("/jobs/approve")
    public ResponseEntity<String> approveJobs(@RequestBody List<Integer> jobIds,
                                              @RequestHeader("Authorization") String jwtToken) {
        try {
            String jwt = jwtToken.replace("Bearer ", ""); // strip "Bearer " prefix if present
            jobService.approveJobs(jobIds, jwt);
            return ResponseEntity.ok("Jobs approved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error approving jobs: " + e.getMessage());
        }
    }

    @PostMapping("/jobs/reject")
    public ResponseEntity<String> rejectJobs(@RequestBody List<Integer> jobIds,
                                             @RequestHeader("Authorization") String jwtToken) {
        try {
            String jwt = jwtToken.replace("Bearer ", "");
            jobService.rejectJobs(jobIds, jwt);
            return ResponseEntity.ok("Jobs rejected successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error rejecting jobs: " + e.getMessage());
        }
    }
    
 // Delete a job by jobId
    @DeleteMapping("/jobs/delete/{jobId}")
    public ResponseEntity<String> deleteJob(@PathVariable("jobId") Integer jobId,
                                            @RequestHeader("Authorization") String jwtToken) {
        try {
            String jwt = jwtToken.replace("Bearer ", "");
            // Validate JWT and user (already done in deleteJob via approve/reject methods)
            boolean deleted = jobService.deleteJob(jobId);
            if (deleted) {
                return ResponseEntity.ok("Job deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found with ID: " + jobId);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error deleting job: " + e.getMessage());
        }
    }

}