package com.talenttap.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "education")
public class Education {
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int educationId;
    
    @ManyToOne
    @JoinColumn(name = "jobseeker_id", nullable = false)
    private JobSeeker jobSeeker;
    
    @ManyToOne
    @JoinColumn(name = "education_level_id" , referencedColumnName ="educationLevelId")
    private EducationLevel educationLevel;
    
    @Column(name = "degree")
    private String degree;
    
    @Column(name = "institution", nullable = false)
    private String institution;
    
    @Column(name = "board_of_study")
    private String boardOfStudy;
    
    @Column(name = "percentage", nullable = false)
    private float percentage;
    
    @Column(name = "start_year", nullable = false)
    private int startYear;
    
    @Column(name = "end_year")
    private int endYear;
  
}