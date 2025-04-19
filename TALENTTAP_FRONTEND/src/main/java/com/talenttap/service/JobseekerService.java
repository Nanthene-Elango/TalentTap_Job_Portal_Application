package com.talenttap.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.talenttap.model.EducationLevel;
import com.talenttap.model.Jobseeker;
import com.talenttap.model.JobseekerRegister;
import com.talenttap.model.JwtToken;
import com.talenttap.model.Location;
import com.talenttap.model.Login;
import com.talenttap.model.Skills;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class JobseekerService {

	@Autowired
	private RestTemplate restTemplate;

	public List<Location> getAllLocations() {

		String url = "http://localhost:8083/api/locations";

		ResponseEntity<Location[]> response = restTemplate.getForEntity(url, Location[].class);
		Location[] locationArray = response.getBody();

		List<Location> locations = Arrays.asList(locationArray);

		return locations;
	}

	public List<Skills> getAllSkills() {

		String url = "http://localhost:8083/api/skills";

		ResponseEntity<Skills[]> response = restTemplate.getForEntity(url, Skills[].class);
		Skills[] skillsArray = response.getBody();

		List<Skills> skills = Arrays.asList(skillsArray);

		return skills;
	}

	public List<EducationLevel> getEducationLevel() {
		String url = "http://localhost:8083/api/educationlevel";

		ResponseEntity<EducationLevel[]> response = restTemplate.getForEntity(url, EducationLevel[].class);
		EducationLevel[] educationLevel = response.getBody();

		return Arrays.asList(educationLevel);
	}

	public void register(JobseekerRegister jobseekerRegister) {

		String url = "http://localhost:8083/auth/register/jobseeker";

		restTemplate.postForEntity(url, jobseekerRegister, String.class);
	}

	public void login(Login login, HttpServletResponse response) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Login> request = new HttpEntity<>(login, headers);

		ResponseEntity<String> backendResponse = restTemplate.exchange("http://localhost:8083/auth/login/jobseeker",
				HttpMethod.POST, request, String.class);
		List<String> cookieHeaders = backendResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
		if (cookieHeaders != null) {
			for (String cookieHeader : cookieHeaders) {
				response.addHeader(HttpHeaders.SET_COOKIE, cookieHeader);
			}
		}

		System.out.println(backendResponse.getBody());
	}

	public String getFullName(JwtToken jwtToken) {

		String url = "http://localhost:8083/jobseeker/fullName";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<JwtToken> request = new HttpEntity<>(jwtToken, headers);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			return response.getBody();
		} else {
			return null;
		}
	}

	public Jobseeker getJobseeker(JwtToken jwt) {
		String url = "http://localhost:8083/jobseeker";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<JwtToken> request = new HttpEntity<>(jwt, headers);

		ResponseEntity<Jobseeker> response = restTemplate.exchange(url, HttpMethod.POST, request, Jobseeker.class);

		return response.getBody();
	}

	public void updateProfilePicture(MultipartFile file, Integer jobSeekerId) {

		try {

			RestTemplate restTemplate = new RestTemplate();

			String backendUrl = "http://localhost:8083/jobseeker/upload-profile-photo";

			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("profilePhoto",
					new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));
			body.add("jobSeekerId", jobSeekerId);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

			// Send the POST request
			ResponseEntity<String> response = restTemplate.postForEntity(backendUrl, requestEntity, String.class);

			if (response.getStatusCode().is2xxSuccessful()) {
				// Success
			} else {
				// Handle failure case
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateProfile(Jobseeker jobSeeker) {
		String url = "http://localhost:8083/jobseeker/update-profile";
		ResponseEntity<String> response = restTemplate.postForEntity(url, jobSeeker , String.class);
	}

	public void updateSummary(String summary, int id) {
		
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put("summary", summary);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestMap, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(
		    "http://localhost:8083/jobseeker/update-summary/" + id,
		    requestEntity,
		    String.class
		);

		System.out.println(response.getBody());
		
	}

}
