package com.talenttap.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
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
@CrossOrigin("*")
public class JobseekerController {

	private LocationService locationService;
	private SkillService skillService;
	private EducationService educationService;
	private JobseekerRegisterService jobseekerService;
	
	public JobseekerController(LocationService location , SkillService skillService, 
			EducationService educationService , JobseekerRegisterService jobseekerService) {
		this.locationService = location;
		this.skillService = skillService;
		this.educationService = educationService;
		this.jobseekerService = jobseekerService;
	}
	
	@GetMapping("api/locations")
	public ResponseEntity<List<LocationDTO>> getAllLocations(){
		return ResponseEntity.ok().body(locationService.getAllLocations());
	}
	
	@GetMapping("api/skills")
	public ResponseEntity<List<SkillsDTO>> getAllSkills(){
		return ResponseEntity.ok().body(skillService.getAllSkills());
	}
	
	@GetMapping("api/educationlevel")
	public ResponseEntity<List<EducationLevelDTO>> getAllEducationLevel(){
		return ResponseEntity.ok().body(educationService.getAllEducationLevel());
	}
	
	@PostMapping("jobseeker/fullName")
	public ResponseEntity<String> getFullName(@RequestBody Map<String,String> jwt){
		return jobseekerService.getFullName(jwt.get("jwt"));
	}
	
	@PostMapping("jobseeker")
	public ResponseEntity<JobseekerDTO> getJobseeker(@RequestBody Map<String,String> jwt){
		return jobseekerService.getJobseeker(jwt.get("jwt"));
	}

	@GetMapping("jobseeker/profile-photo/{id}/{jobseekerId}")
	public ResponseEntity<?> getProfilePhoto(@PathVariable Integer id) {
		return jobseekerService.getProfilePhotoById(id);
	}
	
	@PostMapping("jobseeker/upload-profile-photo")
    public ResponseEntity<String> uploadProfilePhoto(@RequestParam("profilePhoto") MultipartFile file,
                                                     @RequestParam("jobSeekerId") Integer jobSeekerId) {
        return jobseekerService.uploadProfilePicture(file , jobSeekerId);
    }
	
	@PutMapping("jobseeker/update-profile")
	public ResponseEntity<String> updateProfile(@RequestBody JobseekerDTO request){
		return jobseekerService.updateProfile(request);
	}
	
	@PutMapping("jobseeker/update-summary/{id}")
	public ResponseEntity<String> updateSummary(@RequestBody Map<String , String> summary , @PathVariable int id){
		return jobseekerService.updateSummary(summary.get("summary") , id);
	}
	
	@GetMapping("jobseeker/educations/{id}")
	public ResponseEntity<List<EducationDTO>> getAllEducation(@PathVariable Integer id){
		return jobseekerService.getAllEducation(id);
	}
	
	@GetMapping("jobseeker/skills/{id}")
	public ResponseEntity<List<SkillsDTO>> getAllSkillsById(@PathVariable Integer id){
		return jobseekerService.getAllSkillsById(id);
	}
	
	@DeleteMapping("jobseeker/delete/skill/{id}/{jobseekerId}")
	public ResponseEntity<String> deleteSkillById(@PathVariable Integer id , @PathVariable Integer jobseekerId){
		return jobseekerService.deleteSkillById(id , jobseekerId);
	}
	
	@GetMapping("api/jobs")
	public ResponseEntity<List<JobDTO>> getAllJobs(){
		return jobseekerService.getAllJobs();
	}
	
	@GetMapping("api/job/{id}")
	public ResponseEntity<JobDTO> getJobById(@PathVariable int id){
		return jobseekerService.getJobById(id);
	}
	
	@PostMapping("api/jobs/filter")
	public ResponseEntity<List<JobDTO>> filterJobs(@RequestBody JobFilterDTO jobFilter){
		return jobseekerService.filterJobs(jobFilter);
	}
}
