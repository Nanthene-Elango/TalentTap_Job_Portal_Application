package com.talenttap.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.talenttap.DTO.EmailDTO;
import com.talenttap.DTO.EmployerProfileDTO;
import com.talenttap.DTO.EmployerRegisterDTO;
import com.talenttap.model.IndustryType;
import com.talenttap.model.JwtToken;
import com.talenttap.model.Login;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class EmployerAuthService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	public EmployerAuthService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	public void register(EmployerRegisterDTO dto) throws IOException {
		String url = "http://localhost:8083/auth/register/employer";
		
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("fullName", dto.getFullName());
        body.add("username", dto.getUsername());
        body.add("password", dto.getPassword());
        body.add("phoneNumber", dto.getPhoneNumber());
        body.add("emailId", dto.getEmailId());
        body.add("companyName", dto.getCompanyName());
        body.add("industryType", dto.getIndustryType());
        body.add("companyEmail", dto.getCompanyEmail());
        body.add("companyPhoneNumber", dto.getCompanyPhoneNumber());
        body.add("companySize", dto.getCompanySize());
        body.add("locationId", dto.getLocationId());
        body.add("webUrl", dto.getWebUrl());
        body.add("foundedAt", dto.getFoundedAt());
        body.add("about", dto.getAbout());
        body.add("designation", dto.getDesignation());

        if (dto.getCompanyLogo() != null && !dto.getCompanyLogo().isEmpty()) {
            ByteArrayResource fileResource = new ByteArrayResource(dto.getCompanyLogo().getBytes()) {
                @Override
                public String getFilename() {
                    return dto.getCompanyLogo().getOriginalFilename();
                }
            };
            body.add("companyLogo", fileResource);
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(url, requestEntity, String.class);
	}
	
	public List<IndustryType> getAllIndustryType(){
		
		String url = "http://localhost:8083/api/industry";
		
		ResponseEntity<IndustryType[]>  response = restTemplate.getForEntity(url, IndustryType[].class);
		IndustryType[] industryType = response.getBody();
		List<IndustryType> list = Arrays.asList(industryType);
		return list;
		
	}
	
	public EmployerProfileDTO profile(JwtToken jwtToken) {
	    String url = "http://localhost:8083/api/employer/profile";
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<JwtToken> request = new HttpEntity<>(jwtToken, headers);
	    
	    try {
	        ResponseEntity<EmployerProfileDTO> response = restTemplate.exchange(
	            url,
	            HttpMethod.POST,
	            request,
	            EmployerProfileDTO.class
	        );
	        
	        if (response.getStatusCode().is2xxSuccessful()) {
	            return response.getBody();
	        } else {
	            System.err.println("Failed to fetch profile. Status: " + response.getStatusCode());
	            return null;
	        }
	    } catch (Exception e) {
	        System.err.println("Error fetching profile: " + e.getMessage());
	        return null;
	    }
	}
	
	public boolean login(Login login, HttpServletResponse response, Model model) {
	    try {
	    	System.out.println("im reaching login");
	    	System.out.println("Username"+login.getUsername());
	    	System.out.println("Password"+login.getPassword());
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        HttpEntity<Login> request = new HttpEntity<>(login, headers);

	        ResponseEntity<String> backendResponse = restTemplate.exchange(
	            "http://localhost:8083/api/auth/login/employer",
	            HttpMethod.POST,
	            request,
	            String.class
	        );
	        
	        String responseBody = backendResponse.getBody();

	        // Set cookies from backend if present
	        List<String> cookieHeaders = backendResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
	        if (cookieHeaders != null) {
	            for (String cookieHeader : cookieHeaders) {
	                response.addHeader(HttpHeaders.SET_COOKIE, cookieHeader);
	            }
	        }
	        return true;
	    } catch (HttpClientErrorException | HttpServerErrorException ex) {
	        System.out.println("HTTP Status: " + ex.getStatusCode());
	        if(ex.getStatusCode().toString().contains("401")) {
	 	       model.addAttribute("error", "Invalid username/password");
	        }else if(ex.getStatusCode().toString().contains("404")) {
	 
	 	       model.addAttribute("error", "No employer account found");
	        }  
	        return false;
	    }
	}
	
	
	// approve candidate
	
	public void callApproveAPI(int candidateId, EmailDTO emailDTO, String token) {
	    String url = "http://localhost:8083/api/api/candidate/" + candidateId + "/approve";

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setBearerAuth(token); // automatically adds Bearer prefix

	    HttpEntity<EmailDTO> request = new HttpEntity<>(emailDTO, headers);

	    try {
	        ResponseEntity<Void> response = restTemplate.postForEntity(url, request, Void.class);

	        if (!response.getStatusCode().is2xxSuccessful()) {
	            throw new RuntimeException("Approval failed with status: " + response.getStatusCode());
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error occurred while calling approval API: " + e.getMessage());
	    }
	}
	
	public void callRejectAPI(int candidateId, EmailDTO emailDTO, String token) {
	    String url = "http://localhost:8083/api/api/candidate/" + candidateId + "/reject";

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setBearerAuth(token); // automatically adds Bearer prefix

	    HttpEntity<EmailDTO> request = new HttpEntity<>(emailDTO, headers);

	    try {
	        ResponseEntity<Void> response = restTemplate.postForEntity(url, request, Void.class);

	        if (!response.getStatusCode().is2xxSuccessful()) {
	            throw new RuntimeException("rejection failed with status: " + response.getStatusCode());
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error occurred while calling approval API: " + e.getMessage());
	    }
	}

	public boolean handleIdentityUpload(MultipartFile govIdFile, MultipartFile companyIdFile, String companyEmail, String token) {
		 String url = "http://localhost:8083/api/verify/employer/company";
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setBearerAuth(token); // automatically adds Bearer prefix

	    HttpEntity<Void> request = new HttpEntity<>(null, headers);

	    try {
	        ResponseEntity<Boolean> response = restTemplate.postForEntity(url, request, Boolean.class);
	        if (!response.getStatusCode().is2xxSuccessful()) {
	            throw new RuntimeException("rejection failed with status: " + response.getStatusCode());
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error occurred while calling approval API: " + e.getMessage());
	    }
	    return true;
	}

}
