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

import com.talenttap.model.EducationLevel;
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

	public void login(Login login , HttpServletResponse response) {

		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<Login> request = new HttpEntity<>(login, headers);

	    ResponseEntity<String> backendResponse = restTemplate.exchange(
	    		"http://localhost:8083/auth/login/jobseeker",
	            HttpMethod.POST,
	            request,
	            String.class
	    );
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
		
	    ResponseEntity<String> response = restTemplate.exchange(
	    		url,
	            HttpMethod.POST,
	            request,
	            String.class
	    );
		
		if (response.getStatusCode().is2xxSuccessful()) {
			return response.getBody();
		}
		else {
			return null;
		}
	}

}
