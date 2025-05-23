package com.talenttap.service;

import com.talenttap.DTO.LoginDTO;
import com.talenttap.model.Login;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    private final RestTemplate restTemplate;

    @Value("${backend.api.base-url}")
    private String backendBaseUrl;

    public AdminService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String loginAdmin(Login login, HttpServletResponse response) {
        // Map Login to LoginDTO
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(login.getUsername());
        loginDTO.setPassword(login.getPassword());

        try {
            // Prepare request to backend
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<LoginDTO> request = new HttpEntity<>(loginDTO, headers);

            // Call backend API
            ResponseEntity<Map> backendResponse = restTemplate.exchange(
                    backendBaseUrl + "/auth/login/admin",
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            // Check response status
            if (backendResponse.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> responseBody = backendResponse.getBody();
                if (responseBody != null && "Admin login successful".equals(responseBody.get("message"))) {
                    // Forward cookies from backend to client
                    List<String> cookieHeaders = backendResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
                    if (cookieHeaders != null) {
                        for (String cookieHeader : cookieHeaders) {
                            response.addHeader(HttpHeaders.SET_COOKIE, cookieHeader);
                        }
                    }
                    return "SUCCESS";
                }
                return "Invalid credentials";
            }
            return "Login failed: Invalid credentials";
        } catch (Exception e) {
            return "Login failed: " + e.getMessage();
        }
    }
}