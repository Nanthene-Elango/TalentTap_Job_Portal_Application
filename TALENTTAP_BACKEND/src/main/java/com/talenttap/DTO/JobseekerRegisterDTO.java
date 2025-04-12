package com.talenttap.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobseekerRegisterDTO {


    private Integer yearsOfExperience;
    private Integer location;
    private Integer highestQualification;
    private String degree;
    private String institution;
    private Integer startYear;
    private Integer endYear;
    private String boardOfStudy;
    private Float percentage;
    private String fullName;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private List<Integer> skillIds;
    
}
