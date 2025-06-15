package com.talenttap.service;

import com.talenttap.DTO.AdminProfileDTO;
import com.talenttap.DTO.ChangePasswordDTO;
import com.talenttap.DTO.EmployerAdminDTO;
import com.talenttap.DTO.EmployerDetailsDTO;
import com.talenttap.DTO.LoginDTO;
import com.talenttap.model.Login;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
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
                return "Login failed: Invalid response from server";
            }else {
                // Extract the error message from the backend response
                Map<String, Object> responseBody = backendResponse.getBody();
                if (responseBody != null && responseBody.containsKey("error")) {
                    return (String) responseBody.get("error");
                }
                return "Login failed: " + backendResponse.getStatusCode();
            }
        } catch (Exception e) {
            return "Login failed catch: " + e.getMessage();
        }
    }
    
    public AdminProfileDTO getAdminProfile(String jwtToken) {
        try {
            // Set up headers with JWT token
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);

            // Create HTTP entity with headers
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Make GET request to backend API
            ResponseEntity<AdminProfileDTO> response = restTemplate.exchange(
                backendBaseUrl + "/admin/profile",
                HttpMethod.GET,
                entity,
                AdminProfileDTO.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new RuntimeException("Backend returned: " + response.getStatusCode());
            }

        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Invalid or expired JWT token: " + e.getStatusCode(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching admin profile: " + e.getMessage(), e);
        }
    }


    public String changePassword(ChangePasswordDTO changePasswordDTO, String jwtToken) {
        // Set up headers with JWT token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.set("Content-Type", "application/json");

        // Create HTTP entity with headers and body
        HttpEntity<ChangePasswordDTO> entity = new HttpEntity<>(changePasswordDTO, headers);

        // Make POST request to backend API
        ResponseEntity<String> response = restTemplate.exchange(
            backendBaseUrl + "/admin/profile/change-password",
            HttpMethod.POST,
            entity,
            String.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException(response.getBody() != null ? response.getBody() : "Failed to change password: " + response.getStatusCode());
        }
    }
    
    public List<EmployerAdminDTO> getAllEmployers(String jwtToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<EmployerAdminDTO[]> response = restTemplate.exchange(
                    backendBaseUrl + "/admin/employers",
                    HttpMethod.GET,
                    entity,
                    EmployerAdminDTO[].class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            	System.out.println("EmployerAdminDTO response: " + Arrays.toString(response.getBody()));
                return Arrays.asList(response.getBody());
            } else {
                throw new RuntimeException("Failed to fetch employers: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Invalid or expired JWT token: " + e.getStatusCode(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching employers: " + e.getMessage(), e);
        }
    }

    public List<EmployerAdminDTO> searchEmployers(String jwtToken, String status, String industry, String search, LocalDate startDate, LocalDate endDate) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            StringBuilder url = new StringBuilder(backendBaseUrl + "/admin/employers/search?");
            if (status != null && !status.isEmpty()) url.append("status=").append(status).append("&");
            if (industry != null && !industry.isEmpty()) url.append("industry=").append(industry).append("&");
            if (search != null && !search.isEmpty()) url.append("search=").append(search).append("&");
            if (startDate != null) url.append("startDate=").append(startDate).append("&");
            if (endDate != null) url.append("endDate=").append(endDate);

            ResponseEntity<EmployerAdminDTO[]> response = restTemplate.exchange(
                    url.toString(),
                    HttpMethod.GET,
                    entity,
                    EmployerAdminDTO[].class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return Arrays.asList(response.getBody());
            } else {
                throw new RuntimeException("Failed to search employers: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Invalid or expired JWT token: " + e.getStatusCode(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error searching employers: " + e.getMessage(), e);
        }
    }

    public EmployerDetailsDTO getEmployerDetails(String jwtToken, Integer employerId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            System.out.println(employerId);
            ResponseEntity<EmployerDetailsDTO> response = restTemplate.exchange(
                    backendBaseUrl + "/admin/employer/" + employerId,
                    HttpMethod.GET,
                    entity,
                    EmployerDetailsDTO.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to fetch employer details: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Invalid or expired JWT token: " + e.getStatusCode(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching employer details: " + e.getMessage(), e);
        }
    }

    public String verifyEmployers(String jwtToken, List<Integer> employerIds) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<List<Integer>> entity = new HttpEntity<>(employerIds, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    backendBaseUrl + "/admin/employers/verify",
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new RuntimeException(response.getBody() != null ? response.getBody() : "Failed to verify employers: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Invalid or expired JWT token: " + e.getStatusCode(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error verifying employers: " + e.getMessage(), e);
        }
    }

    public String unverifyEmployers(String jwtToken, List<Integer> employerIds) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<List<Integer>> entity = new HttpEntity<>(employerIds, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    backendBaseUrl + "/admin/employers/unverify",
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new RuntimeException(response.getBody() != null ? response.getBody() : "Failed to unverify employers: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Invalid or expired JWT token: " + e.getStatusCode(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error unverifying employers: " + e.getMessage(), e);
        }
    }
    
    public String verifySingleEmployer(String jwtToken, Integer employerId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Integer> entity = new HttpEntity<>(employerId, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    backendBaseUrl + "/admin/employer/verify-single",
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new RuntimeException(response.getBody() != null ? response.getBody() : "Failed to verify employer: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Invalid or expired JWT token: " + e.getStatusCode(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error verifying employer: " + e.getMessage(), e);
        }
    }

    public String unverifySingleEmployer(String jwtToken, Integer employerId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Integer> entity = new HttpEntity<>(employerId, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    backendBaseUrl + "/admin/employer/unverify-single",
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new RuntimeException(response.getBody() != null ? response.getBody() : "Failed to unverify employer: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Invalid or expired JWT token: " + e.getStatusCode(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error unverifying employer: " + e.getMessage(), e);
        }
    }
}