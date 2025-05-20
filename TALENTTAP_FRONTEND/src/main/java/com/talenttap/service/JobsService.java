package com.talenttap.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.talenttap.DTO.AdminJobDTO;
import com.talenttap.DTO.JobDisplayDTO;
import com.talenttap.DTO.JobFormDTO;
import com.talenttap.model.EmploymentType;
import com.talenttap.model.JobCategory;
import com.talenttap.model.Jobs;
import com.talenttap.model.JwtToken;

@Service
public class JobsService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${backend.api.base-url}")
    private String backendBaseUrl;
	
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


	public List<Jobs> getAllJobs() {
		String url = "http://localhost:8083/api/jobs";
		
		ResponseEntity<Jobs[]> response = restTemplate.getForEntity(url, Jobs[].class);
		
		if (response.getStatusCode().is2xxSuccessful()) {
			return Arrays.asList(response.getBody());
		}
		else {
			return null;
		}
	}
	
	public JobFormDTO getJobById(int jobId, JwtToken jwtToken) {
        String url = "http://localhost:8083/jobs/" + jobId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + jwtToken.getJwt());

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<JobFormDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                JobFormDTO.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                System.err.println("Failed to fetch job. Status: " + response.getStatusCode());
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error fetching job: " + e.getMessage());
            return null;
        }
    }

    public ResponseEntity<String> updateJob(JobFormDTO jobFormDTO, String jwt) {
        String url = "http://localhost:8083/jobs/update";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (jwt != null && !jwt.trim().isEmpty()) {
            headers.set("Authorization", "Bearer " + jwt.trim());
        } else {
            throw new IllegalArgumentException("JWT token is required for authentication");
        }

        HttpEntity<JobFormDTO> request = new HttpEntity<>(jobFormDTO, headers);

        return restTemplate.exchange(
            url,
            HttpMethod.PUT,
            request,
            String.class
        );
    }
    
 // Fetch all jobs from the backend
    public List<AdminJobDTO> getAllAdminJobs(String jwt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + jwt);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<AdminJobDTO>> response = restTemplate.exchange(
                    backendBaseUrl + "/admin/jobs",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<AdminJobDTO>>() {}
            );
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

//    // Fetch a single job by ID
    public JobDisplayDTO getJobById(int jobId, String jwt) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<JobDisplayDTO> response = restTemplate.exchange(
                    backendBaseUrl + "/admin/jobs" + jobId,
                    HttpMethod.GET,
                    entity,
                    JobDisplayDTO.class
            );
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Approve jobs
    public void approveJobs(List<Integer> jobIds, String jwt) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<Integer>> entity = new HttpEntity<>(jobIds, headers);

        try {
            restTemplate.exchange(
                    backendBaseUrl + "/admin/jobs/approve",
                    HttpMethod.POST,
                    entity,
                    Void.class
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Reject jobs
    public void rejectJobs(List<Integer> jobIds, String jwt) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<Integer>> entity = new HttpEntity<>(jobIds, headers);

        try {
            restTemplate.exchange(
                    backendBaseUrl + "/admin/jobs/reject",
                    HttpMethod.POST,
                    entity,
                    Void.class
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
