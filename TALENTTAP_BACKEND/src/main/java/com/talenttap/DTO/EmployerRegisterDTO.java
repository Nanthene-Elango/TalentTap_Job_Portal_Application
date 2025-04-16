// com.talenttap.DTO.EmployerRegisterDTO
package com.talenttap.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

@Data
public class EmployerRegisterDTO {
    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Username is required")
    @Size(min = 3, message = "Username must be at least 3 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String emailId;

    @NotBlank(message = "Company name is required")
    private String companyName;

    private MultipartFile companyLogo; // Optional

    @NotNull(message = "Industry type is required")
    private Integer industryType;

    @NotBlank(message = "Company email is required")
    @Email(message = "Invalid company email format")
    private String companyEmail;

    @NotBlank(message = "Company phone number is required")
    @Pattern(regexp = "\\d{10}", message = "Company phone number must be 10 digits")
    private String companyPhoneNumber;

    @NotBlank(message = "Company size is required")
    private String companySize;

    @NotNull(message = "Location is required")
    private Integer locationId;

    @NotBlank(message = "Website URL is required")
    @URL(message = "Invalid URL format")
    private String webUrl;

    @NotNull(message = "Founded year is required")
    @Min(value = 1800, message = "Founded year must be after 1800")
    @Max(value = 2025, message = "Founded year cannot be in the future")
    private Integer foundedAt;

    @NotBlank(message = "Company description is required")
    private String about;

    @NotBlank(message = "Designation is required")
    private String designation;
}