package com.talenttap.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.talenttap.model.CompanyProfile;
import com.talenttap.model.Jobseeker;

@Service
public class CompanyService {

	private RestTemplate restTemplate;

	public CompanyService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public CompanyProfile companyProfile(String token) {
		String url = "http://localhost:8083/api/employer/company/profile";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(token);

		HttpEntity<String> entity = new HttpEntity<>(headers);

		try {
			ResponseEntity<CompanyProfile> response = restTemplate.exchange(url, HttpMethod.GET, entity,
					CompanyProfile.class);

			if (response.getStatusCode().is2xxSuccessful()) {
				response.getBody().getCompanyName();
				return response.getBody();
			} else {
				System.err.println("Failed to fetch company profile. Status: " + response.getStatusCode());
				return null;
			}
		} catch (Exception e) {
			System.err.println("Error fetching profile: " + e.getMessage());
			return null;
		}
	}

	public String updateProfile(CompanyProfile companyProfile, String token) {
		String url = "http://localhost:8083/api/employer/company/update";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(token);

		HttpEntity<CompanyProfile> entity = new HttpEntity<>(companyProfile,headers);

		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity,
					String.class);

			if (response.getStatusCode().is2xxSuccessful()) {
				response.getBody();
				return response.getBody();
			} else {
				System.err.println("Failed to fetch company profile. Status: " + response.getStatusCode());
				return null;
			}
		} catch (Exception e) {
			System.err.println("Error fetching profile: " + e.getMessage());
			return null;
		}
	}
	
	public String companyLogo(String token) {
		String url = "http://localhost:8083/api/employer/company/logo";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(token);

		HttpEntity<String> entity = new HttpEntity<>(headers);

		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity,
					String.class);

			if (response.getStatusCode().is2xxSuccessful()) {
				response.getBody();
				return response.getBody();
			} else {
				System.err.println("Failed to fetch company profile. Status: " + response.getStatusCode());
				return null;
			}
		} catch (Exception e) {
			System.err.println("Error fetching profile: " + e.getMessage());
			return null;
		}
	}

	public String updateCompanyLogo(byte[] logoBytes, String jwt) {
		String url = "http://localhost:8083/api/employer/company/logo/update";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(jwt);

		HttpEntity<byte[]> entity = new HttpEntity<>(logoBytes,headers);

		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity,
					String.class);

			if (response.getStatusCode().is2xxSuccessful()) {
				return response.getBody();
			} else {
				System.err.println("Failed to fetch company profile. Status: " + response.getStatusCode());
				return null;
			}
		} catch (Exception e) {
			System.err.println("Error fetching profile: " + e.getMessage());
			return null;
		}
		
	}
	
	

}
