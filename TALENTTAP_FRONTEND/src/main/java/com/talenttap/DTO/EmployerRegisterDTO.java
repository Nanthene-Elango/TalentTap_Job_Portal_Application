// com.talenttap.DTO.EmployerRegisterDTO
package com.talenttap.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class EmployerRegisterDTO {
    private String fullName;

    private String username;

    private String password;
    
    private String phoneNumber;

    private String emailId;

    private String companyName;

    private MultipartFile companyLogo;

    private Integer industryType;

    private String companyEmail;

    private String companyPhoneNumber;

    private String companySize;

    private Integer locationId;

    private String webUrl;
    
    private Integer foundedAt;

    private String about;

    private String designation;
}