package com.talenttap.service;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.talenttap.DTO.CandidatesDTO;
import com.talenttap.DTO.EditJobFormDTO;
import com.talenttap.DTO.JobDisplayDTO;
import com.talenttap.DTO.JobFormDTO;
import com.talenttap.exception.JobFetchException;
import com.talenttap.model.EmploymentType;
import com.talenttap.model.JobCategory;
import com.talenttap.model.Jobs;
import com.talenttap.model.JwtToken;

import jakarta.validation.Valid;

@Service
public class JobsService {

	@Autowired
	private RestTemplate restTemplate;

	private static final Logger logger = LoggerFactory.getLogger(JobsService.class);

	public List<EmploymentType> getEmploymentType() {
		String url = "http://localhost:8083/jobs/jobtype";

		ResponseEntity<EmploymentType[]> response = restTemplate.getForEntity(url, EmploymentType[].class);
		EmploymentType[] employmentType = response.getBody();

		return Arrays.asList(employmentType);
	}

	public List<JobCategory> getJobCategories() {
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
		return restTemplate.exchange(url, HttpMethod.POST, request, String.class);
	}
	
    public ResponseEntity<String> updateJob(@Valid EditJobFormDTO jobFormDTO, String jwt) {
    	String url = "http://localhost:8083/jobs/job/edit";

		// Set headers with JWT token
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		if (jwt != null && !jwt.trim().isEmpty()) {
			headers.set("Authorization", "Bearer " + jwt.trim());
		} else {
			throw new IllegalArgumentException("JWT token is required for authentication");
		}

		// Create request entity with JobFormDTO as the body
		HttpEntity<EditJobFormDTO> request = new HttpEntity<>(jobFormDTO, headers);

		// Send POST request and return response
		return restTemplate.exchange(url, HttpMethod.POST, request, String.class);
	}
	
	
	
	
	
	
	
	
	
	
	// get dashboard jobs
	public List<JobDisplayDTO> getAllDashBoardJobs(JwtToken jwtToken) {
	    String url = "http://localhost:8083/jobs/dashboard/jobs";

	    logger.info("Calling job service to fetch jobs");

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.set("Authorization", "Bearer " + jwtToken.getJwt());

	    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

	    try {
	        ResponseEntity<JobDisplayDTO[]> response = restTemplate.exchange(
	                url,
	                HttpMethod.POST,
	                requestEntity,
	                JobDisplayDTO[].class
	        );

	        if (response.getStatusCode().is2xxSuccessful()) {
	            JobDisplayDTO[] jobs = response.getBody();
	            logger.info("Fetched {} jobs successfully", (jobs == null ? 0 : jobs.length));
	            if(jobs == null) {
	            	
	            }
	            return Arrays.asList(jobs != null ? jobs : new JobDisplayDTO[0]);
	        } else {
	            logger.error("Failed to fetch jobs. Status: {}", response.getStatusCode());
	            throw new JobFetchException("Failed to fetch jobs with status: " + response.getStatusCode());
	        }

	    } catch (HttpClientErrorException | HttpServerErrorException ex) {
	        logger.error("HTTP error when fetching jobs: status={}, responseBody={}", ex.getStatusCode(), ex.getResponseBodyAsString());
	        throw new JobFetchException("HTTP error fetching jobs: " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString());
	    } catch (ResourceAccessException ex) {
	        logger.error("Resource access exception when fetching jobs: {}", ex.getMessage());
	        throw new JobFetchException("Resource access error fetching jobs: " + ex.getMessage());
	    } catch (Exception ex) {
	        logger.error("Unexpected error when fetching jobs", ex);
	        throw new JobFetchException("Unexpected error fetching jobs: " + ex.getMessage());
	    }
	}

	
	
	
	
	
	
	
	
	
	
	
	
	public List<JobDisplayDTO> getAllJobs(JwtToken jwtToken) {
	    String url = "http://localhost:8083/jobs/getAllJobs";

	    logger.info("Calling job service to fetch jobs");

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.set("Authorization", "Bearer " + jwtToken.getJwt());

	    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

	    try {
	        ResponseEntity<JobDisplayDTO[]> response = restTemplate.exchange(
	                url,
	                HttpMethod.GET,
	                requestEntity,
	                JobDisplayDTO[].class
	        );

	        if (response.getStatusCode().is2xxSuccessful()) {
	            JobDisplayDTO[] jobs = response.getBody();
	            logger.info("Fetched {} jobs successfully", (jobs == null ? 0 : jobs.length));
	            if(jobs == null) {
	            	
	            }
	            return Arrays.asList(jobs != null ? jobs : new JobDisplayDTO[0]);
	        } else {
	            logger.error("Failed to fetch jobs. Status: {}", response.getStatusCode());
	            throw new JobFetchException("Failed to fetch jobs with status: " + response.getStatusCode());
	        }

	    } catch (HttpClientErrorException | HttpServerErrorException ex) {
	        logger.error("HTTP error when fetching jobs: status={}, responseBody={}", ex.getStatusCode(), ex.getResponseBodyAsString());
	        throw new JobFetchException("HTTP error fetching jobs: " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString());
	    } catch (ResourceAccessException ex) {
	        logger.error("Resource access exception when fetching jobs: {}", ex.getMessage());
	        throw new JobFetchException("Resource access error fetching jobs: " + ex.getMessage());
	    } catch (Exception ex) {
	        logger.error("Unexpected error when fetching jobs", ex);
	        throw new JobFetchException("Unexpected error fetching jobs: " + ex.getMessage());
	    }
	}

	public List<Jobs> getAllJobs() {
		String url = "http://localhost:8083/api/jobs";

		ResponseEntity<Jobs[]> response = restTemplate.getForEntity(url, Jobs[].class);

		if (response.getStatusCode().is2xxSuccessful()) {
			return Arrays.asList(response.getBody());
		} else {
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
			ResponseEntity<JobFormDTO> response = restTemplate.exchange(url, HttpMethod.GET, request, JobFormDTO.class);

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

		return restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
	}
	
	public String toggleJobStatus(int id) {
        String apiUrl = "http://localhost:8083/jobs/api/job/" + id + "/toggleStatus";
        System.out.println("Reaching the service");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.PUT,  // use PATCH here because backend expects PATCH
                entity,
                String.class
        );
        System.out.println(response.getBody());
        return response.getBody();
    }

	public boolean deleteJob(int id) {
	    String apiUrl = "http://localhost:8083/jobs/" + id + "/delete";
	    System.out.println("Reaching the service from delete");

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

	    HttpEntity<Void> entity = new HttpEntity<>(headers);

	    try {
	        ResponseEntity<Void> response = restTemplate.exchange(
	                apiUrl,
	                HttpMethod.DELETE,  // or PATCH if backend expects PATCH
	                entity,
	                Void.class
	        );

	        System.out.println("Response status: " + response.getStatusCode());
	        // Optionally check for 204 No Content or 200 OK
	        return response.getStatusCode().is2xxSuccessful();

	    } catch (HttpClientErrorException e) {
	        // Handle 4xx errors here (e.g., 404 Not Found, 400 Bad Request)
	        System.err.println("Client error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
	    } catch (HttpServerErrorException e) {
	        // Handle 5xx errors here
	        System.err.println("Server error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
	    } catch (RestClientException e) {
	        // Handle other errors like I/O, connection refused, etc.
	        System.err.println("Error during REST call: " + e.getMessage());
	    }

	    return false;  // return false if delete failed
	}
	
	
	
	// get all applied candidates
	
	public List<CandidatesDTO> getAllAppliedCandidates(JwtToken jwtToken) throws Exception {
	    String url = "http://localhost:8083/api/employer/candidates";

	    logger.info("Calling job service to fetch applied candidates");

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.set("Authorization", "Bearer " + jwtToken.getJwt());

	    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

	    try {
	        ResponseEntity<CandidatesDTO[]> response = restTemplate.exchange(
	                url,
	                HttpMethod.GET,
	                requestEntity,
	                CandidatesDTO[].class
	        );

	        if (response.getStatusCode().is2xxSuccessful()) {
	        	CandidatesDTO[] jobs = response.getBody();
	            logger.info("Fetched {} applied candidates successfully", (jobs == null ? 0 : jobs.length));
	            if(jobs == null) {
	            	
	            }
	            return Arrays.asList(jobs != null ? jobs : new CandidatesDTO[0]);
	        } else {
	            logger.error("Failed to fetch applied candidates. Status: {}", response.getStatusCode());
	            throw new Exception("Failed to fetch applied candidates with status: " + response.getStatusCode());
	        }

	    } catch (HttpClientErrorException | HttpServerErrorException ex) {
	        logger.error("HTTP error when fetching jobs: status={}, responseBody={}", ex.getStatusCode(), ex.getResponseBodyAsString());
	        throw new Exception("HTTP error fetching applied candidates: " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString());
	    } catch (ResourceAccessException ex) {
	        logger.error("Resource access exception when fetching jobs: {}", ex.getMessage());
	        throw new Exception("Resource access error applied candidates: " + ex.getMessage());
	    } catch (Exception ex) {
	        logger.error("Unexpected error when fetching jobs", ex);
	        throw new Exception("Unexpected error applied candidates: " + ex.getMessage());
	    }
	}

	
}
