package com.talenttap.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.talenttap.DTO.JobDisplayDTO;
import com.talenttap.DTO.JobFormDTO;
import com.talenttap.model.EmploymentType;
import com.talenttap.model.JobCategory;
import com.talenttap.model.JwtToken;

@Service
public class JobsService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	public List<EmploymentType> getEmploymentType() {
		String url = "http://localhost:8083/jobs/jobtype";

		ResponseEntity<EmploymentType[]> response = restTemplate.getForEntity(url, EmploymentType[].class);
		EmploymentType[] employmentType = response.getBody();

		return Arrays.asList(employmentType);
	}
	
	
	public List<JobCategory> getJobCategories(){
		String url = "http://localhost:8083/jobs/jobCategory";
		ResponseEntity<JobCategory[]> response = restTemplate.getForEntity(url, JobCategory[].class);
		JobCategory[] jobCategory = response.getBody();
		return Arrays.asList(jobCategory);
	}
	
	public ResponseEntity<String> addJob(JobFormDTO jobFormDTO, String jwt) {
		String url = "http://localhost:8083/jobs/post-job";

		// Set headers with JWT token
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		if (jwt != null && !jwt.trim().isEmpty()) {
			headers.set("Authorization", "Bearer " + jwt.trim());
		} else {
			throw new IllegalArgumentException("JWT token is required for authentication");
		}

		// Create request entity with JobFormDTO as the body
		HttpEntity<JobFormDTO> request = new HttpEntity<>(jobFormDTO, headers);

		// Send POST request and return response
		return restTemplate.exchange(
			url,
			HttpMethod.POST,
			request,
			String.class
		);
	}
	
	public List<JobDisplayDTO> getAllJobs(JwtToken jwtToken){
		 String url = "http://localhost:8083/jobs/jobs";
		    
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    HttpEntity<JwtToken> request = new HttpEntity<>(jwtToken, headers);
		    
		    try {
		        ResponseEntity<JobDisplayDTO[]> response = restTemplate.exchange(
		            url,
		            HttpMethod.POST,
		            request,
		            JobDisplayDTO[].class
		        );
		        
		        JobDisplayDTO[] jobs = response.getBody();
		        
		        
		        if (response.getStatusCode().is2xxSuccessful()) {
		            return Arrays.asList(jobs);
		        } else {
		            System.err.println("Failed to fetch profile. Status: " + response.getStatusCode());
		            return null;
		        }
		    } catch (Exception e) {
		        System.err.println("Error fetching profile: " + e.getMessage());
		        return null;
		    }
	}
}
