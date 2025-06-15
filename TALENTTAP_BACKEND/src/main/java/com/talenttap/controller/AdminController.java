package com.talenttap.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.talenttap.DTO.AdminJobDTO;
import com.talenttap.DTO.AdminProfileDTO;
import com.talenttap.DTO.ChangePasswordDTO;
import com.talenttap.DTO.EmployerAdminDTO;
import com.talenttap.DTO.EmployerDetailsDTO;
import com.talenttap.DTO.JobSeekerAdminDTO;
import com.talenttap.DTO.JobSeekerDetailsDTO;
import com.talenttap.service.AdminService;
import com.talenttap.service.JobService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	private JobService jobService;
	
	@Autowired
    private AdminService adminService;
	
	public AdminController(JobService jobService) {
		this.jobService = jobService;
	}
	
//    @GetMapping("/verify")
//    public ResponseEntity<?> verifyAdmin() {
//        return ResponseEntity.ok("Authorized");
//    }


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
    
    @GetMapping("/profile")
    public ResponseEntity<?> getAdminProfile(@RequestHeader("Authorization") String jwtToken) {
        try {
            String jwt = jwtToken.replace("Bearer ", "");
            AdminProfileDTO adminProfile = adminService.getAdminProfile(jwt);
            return ResponseEntity.ok(adminProfile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to load admin profile: " + e.getMessage());
        }
    }

    // Handle password change with JWT authentication
    @PostMapping("/profile/change-password")
    public ResponseEntity<String> changePassword(
    		@RequestBody ChangePasswordDTO changePasswordDTO,
            @RequestHeader("Authorization") String jwtToken) {
        try {
            String jwt = jwtToken.replace("Bearer ", "");
            adminService.changePassword(changePasswordDTO.getOldPassword(),
                    changePasswordDTO.getNewPassword(),
                    changePasswordDTO.getConfirmNewPassword(), jwt);
            return ResponseEntity.ok("Password changed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error changing password: " + e.getMessage());
        }
    }

 // Employer Section Endpoints
    @GetMapping("/employers")
    public ResponseEntity<List<EmployerAdminDTO>> getAllEmployers(
            @RequestHeader("Authorization") String jwtToken) {
        try {
            String jwt = jwtToken.replace("Bearer ", "");
            List<EmployerAdminDTO> employers = adminService.getAllEmployers(jwt);
            return ResponseEntity.ok(employers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/employers/search")
    public ResponseEntity<List<EmployerAdminDTO>> searchEmployers(
            @RequestHeader("Authorization") String jwtToken,
            @RequestParam(value = "status", required = false) String statusFilter,
            @RequestParam(value = "industry", required = false) String industryFilter,
            @RequestParam(value = "search", required = false) String searchTerm,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            String jwt = jwtToken.replace("Bearer ", "");
            List<EmployerAdminDTO> employers = adminService.searchEmployers(jwt, statusFilter, industryFilter, searchTerm, startDate, endDate);
            return ResponseEntity.ok(employers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
    @GetMapping("/employer/{id}")
    public ResponseEntity<EmployerDetailsDTO> getEmployerDetails(@PathVariable("id") int employerId,
                                                                @RequestHeader("Authorization") String jwtToken) {
        try {
            String jwt = jwtToken.replace("Bearer ", "");
            EmployerDetailsDTO employerDetails = adminService.getEmployerDetails(employerId, jwt);
            return ResponseEntity.ok(employerDetails);
        } catch (Exception e) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/employers/verify")
    public ResponseEntity<String> verifyEmployers(@RequestBody List<Integer> employerIds,
                                                 @RequestHeader("Authorization") String jwtToken) {
        try {
            String jwt = jwtToken.replace("Bearer ", "");
            adminService.updateVerificationStatus(employerIds, true, jwt);
            return ResponseEntity.ok("Selected employers set to Active");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error verifying employers: " + e.getMessage());
        }
    }

    @PostMapping("/employers/unverify")
    public ResponseEntity<String> unverifyEmployers(@RequestBody List<Integer> employerIds,
                                                   @RequestHeader("Authorization") String jwtToken) {
        try {
            String jwt = jwtToken.replace("Bearer ", "");
            adminService.updateVerificationStatus(employerIds, false, jwt);
            return ResponseEntity.ok("Selected employers set to Inactive");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error unverifying employers: " + e.getMessage());
        }
    }
    
    @PostMapping("/employer/verify-single")
    public ResponseEntity<String> verifySingleEmployer(@RequestBody Integer employerId,
                                                      @RequestHeader("Authorization") String jwtToken) {
        try {
            String jwt = jwtToken.replace("Bearer ", "");
            adminService.updateVerificationStatus(List.of(employerId), true, jwt);
            return ResponseEntity.ok("Employer set to Active");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error verifying employer: " + e.getMessage());
        }
    }

    @PostMapping("/employer/unverify-single")
    public ResponseEntity<String> unverifySingleEmployer(@RequestBody Integer employerId,
                                                        @RequestHeader("Authorization") String jwtToken) {
        try {
            String jwt = jwtToken.replace("Bearer ", "");
            adminService.updateVerificationStatus(List.of(employerId), false, jwt);
            return ResponseEntity.ok("Employer set to Inactive");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error unverifying employer: " + e.getMessage());
        }
    }
    
    @GetMapping("/jobseekers")
    public ResponseEntity<List<JobSeekerAdminDTO>> getAllJobSeekers(
            @RequestHeader("Authorization") String jwtToken) {
        try {
            String jwt = jwtToken.replace("Bearer ", "");
            List<JobSeekerAdminDTO> jobSeekers = adminService.getAllJobSeekers(jwt);
            return ResponseEntity.ok(jobSeekers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/jobseeker/{id}")
    public ResponseEntity<JobSeekerDetailsDTO> getJobSeekerDetails(@PathVariable("id") int jobSeekerId,
                                                                  @RequestHeader("Authorization") String jwtToken) {
        try {
            String jwt = jwtToken.replace("Bearer ", "");
            JobSeekerDetailsDTO jobSeekerDetails = adminService.getJobSeekerDetails(jobSeekerId, jwt);
            return ResponseEntity.ok(jobSeekerDetails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}