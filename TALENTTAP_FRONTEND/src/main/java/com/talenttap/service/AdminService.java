package com.talenttap.service;

import com.talenttap.DTO.LoginDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AdminService {

    private final RestTemplate restTemplate;

    @Value("${backend.api.base-url}")
    private String backendBaseUrl;

    public AdminService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> loginAdmin(LoginDTO loginDTO) {
        String url = backendBaseUrl + "/auth/login/admin";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<LoginDTO> request = new HttpEntity<>(loginDTO, headers);
        return restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }
}