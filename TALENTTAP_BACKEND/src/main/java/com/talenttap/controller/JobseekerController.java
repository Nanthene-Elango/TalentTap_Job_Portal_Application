package com.talenttap.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.talenttap.DTO.AddEducationDTO;
import com.talenttap.DTO.Certifications;
import com.talenttap.DTO.EducationDTO;
import com.talenttap.DTO.EducationLevelDTO;
import com.talenttap.DTO.JobseekerDTO;
import com.talenttap.DTO.LocationDTO;
import com.talenttap.DTO.SkillsDTO;
import com.talenttap.DTO.JobDTO;
import com.talenttap.DTO.JobFilterDTO;
import com.talenttap.service.EducationService;
import com.talenttap.service.JobseekerRegisterService;
import com.talenttap.service.LocationService;
import com.talenttap.service.SkillService;

@RestController
@CrossOrigin(origins = "*")
public class JobseekerController {

	private LocationService locationService;
	private SkillService skillService;
	private EducationService educationService;
	private JobseekerRegisterService jobseekerService;

	public JobseekerController(LocationService location, SkillService skillService, EducationService educationService,
			JobseekerRegisterService jobseekerService) {
		this.locationService = location;
		this.skillService = skillService;
		this.educationService = educationService;
		this.jobseekerService = jobseekerService;
	}

	@GetMapping("api/locations")
	public ResponseEntity<List<LocationDTO>> getAllLocations() {
		return ResponseEntity.ok().body(locationService.getAllLocations());
	}

	@GetMapping("api/skills")
	public ResponseEntity<List<SkillsDTO>> getAllSkills() {
		return ResponseEntity.ok().body(skillService.getAllSkills());
	}

	@GetMapping("api/educationlevel")
	public ResponseEntity<List<EducationLevelDTO>> getAllEducationLevel() {
		return ResponseEntity.ok().body(educationService.getAllEducationLevel());
	}

	@PostMapping("jobseeker/fullName")
	public ResponseEntity<String> getFullName(@RequestBody Map<String, String> jwt) {
		return jobseekerService.getFullName(jwt.get("jwt"));
	}

	@PostMapping("jobseeker")
	public ResponseEntity<JobseekerDTO> getJobseeker(@RequestBody Map<String, String> jwt) {
		return jobseekerService.getJobseeker(jwt.get("jwt"));
	}

	@GetMapping("jobseeker/profile-photo/{id}")
	public ResponseEntity<?> getProfilePhoto(@PathVariable Integer id) {
		return jobseekerService.getProfilePhotoById(id);
	}

	@PostMapping("jobseeker/upload-profile-photo")
	public ResponseEntity<String> uploadProfilePhoto(@RequestParam("profilePhoto") MultipartFile file,
			@RequestParam("jobSeekerId") Integer jobSeekerId) {
		return jobseekerService.uploadProfilePicture(file, jobSeekerId);
	}

	@PutMapping("jobseeker/update-profile")
	public ResponseEntity<String> updateProfile(@RequestBody JobseekerDTO request) {
		return jobseekerService.updateProfile(request);
	}

	@PutMapping("jobseeker/update-summary/{id}")
	public ResponseEntity<String> updateSummary(@RequestBody Map<String, String> summary, @PathVariable int id) {
		return jobseekerService.updateSummary(summary.get("summary"), id);
	}

	@GetMapping("jobseeker/educations/{id}")
	public ResponseEntity<List<EducationDTO>> getAllEducation(@PathVariable Integer id) {
		return jobseekerService.getAllEducation(id);
	}

	@GetMapping("jobseeker/skills/{id}")
	public ResponseEntity<List<SkillsDTO>> getAllSkillsById(@PathVariable Integer id) {
		return jobseekerService.getAllSkillsById(id);
	}

	@DeleteMapping("jobseeker/delete/skill/{id}/{jobseekerId}")
	public ResponseEntity<String> deleteSkillById(@PathVariable Integer id, @PathVariable Integer jobseekerId) {
		return jobseekerService.deleteSkillById(id, jobseekerId);
	}

	@PostMapping("jobseeker/resume/upload")
	public ResponseEntity<String> uploadResume(@RequestParam("file") MultipartFile file,
			@CookieValue(value = "jwt", required = false) String jwt) {
		return jobseekerService.uploadResume(file, jwt);
	}

	@DeleteMapping("jobseeker/resume/delete")
	public ResponseEntity<Void> deleteResume(@CookieValue(value = "jwt", required = false) String jwt) {
		return jobseekerService.deleteResume(jwt);
	}

	@GetMapping("jobseeker/resume")
	public ResponseEntity<?> downloadResume(@CookieValue(value = "jwt", required = false) String jwt) {
		return jobseekerService.getResume(jwt);
	}

	@GetMapping("api/jobs")
	public ResponseEntity<List<JobDTO>> getAllJobs() {
		return jobseekerService.getAllJobs();
	}

	@GetMapping("api/job/{id}")
	public ResponseEntity<JobDTO> getJobById(@PathVariable int id) {
		return jobseekerService.getJobById(id);
	}

	@PostMapping("api/jobs/filter")
	public ResponseEntity<List<JobDTO>> filterJobs(@RequestBody JobFilterDTO jobFilter) {
		return jobseekerService.filterJobs(jobFilter);
	}

	@PostMapping("/jobseeker/job/apply/{jobId}")
	public ResponseEntity<String> applyForJob(@CookieValue("jwt") String jwt, @PathVariable int jobId) {
		return jobseekerService.applyJob(jwt, jobId);
	}

	@GetMapping("/jobseeker/job/is-applied/{jobId}")
	public ResponseEntity<Boolean> isJobAlreadyApplied(@CookieValue("jwt") String jwt, @PathVariable int jobId) {
		return jobseekerService.hasApplied(jwt, jobId);
	}

	@GetMapping("api/check-username")
	public ResponseEntity<?> checkUsername(@RequestParam String username) {
		if (username == null || username.trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Username parameter is missing or empty");
		}
		else {
			return jobseekerService.existByUsername(username);
		}
	}
	
	@GetMapping("api/check-email")
	public ResponseEntity<?> checkEmail(@RequestParam String email) {
		if (email == null || email.trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Email parameter is missing or empty");
		}
		else {
			return jobseekerService.existByEmail(email);
		}
	}
	
	@PostMapping("jobseeker/education/add/{id}")
	public ResponseEntity<String> addEducation(@PathVariable int id , @RequestBody AddEducationDTO education){
		return jobseekerService.addEducation(id , education);
	}
	
	@PutMapping("/jobseeker/education/update/{id}")
	public ResponseEntity<String> updateEducation(@PathVariable int id, @RequestBody EducationDTO updatedEducation) {
	    return jobseekerService.updateEducation(id, updatedEducation);
	}
	
	@DeleteMapping("/jobseeker/education/delete/{id}")
	public ResponseEntity<String> deleteEducation(@PathVariable int id){
		return jobseekerService.deleteEducation(id );
	}
	
	@PostMapping("jobseeker/skill/add/{id}")
	public ResponseEntity<String> addSkills(@PathVariable int id,
	                                        @RequestParam("skillIds") List<Integer> skillIds) {
	    return jobseekerService.addSkill(id, skillIds);
	}
	
	@GetMapping("jobseeker/certifications/{id}")
	public ResponseEntity<List<Certifications>> getAllCertifications(@PathVariable Integer id) {
		return jobseekerService.getAllCertifications(id);
	}
	
	@PostMapping("jobseeker/certification/add/{id}")
	public ResponseEntity<String> addCertification(@PathVariable int id , @RequestBody Certifications certification){
		return jobseekerService.addCertification(id , certification);
	}
	
	@PutMapping("jobseeker/certification/update/{id}")
	public ResponseEntity<String> updateCertification(@PathVariable int id, @RequestBody Certifications certification) {
	    return jobseekerService.updateCertification(id, certification);
	}
	
	@DeleteMapping("/jobseeker/certification/delete/{id}")
	public ResponseEntity<String> deleteCertification(@PathVariable int id){
		return jobseekerService.deleteCertification(id );
	}
}
