package com.talenttap.model;

import lombok.Data;

@Data
public class EmployerRegister {
    private String fullName;
    private String username;
    private String password;
    private String phoneNumber;
    private String emailId;
    private String companyName;
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