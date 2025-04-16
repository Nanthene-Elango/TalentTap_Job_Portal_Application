package com.talenttap.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.talenttap.DTO.EmployerRegisterDTO;
import com.talenttap.model.EmployerRegister;
import com.talenttap.model.IndustryType;

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
	
	
	
	

}
