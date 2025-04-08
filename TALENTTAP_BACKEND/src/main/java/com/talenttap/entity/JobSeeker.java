package com.talenttap.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class JobSeeker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jobseeker_id")
    private int jobSeekerId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false , unique =true)
    private Users user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(name = "dob")
    private LocalDate dob;

    @ManyToOne
    @JoinColumn(name = "location")
    private Location location;

    @Column(name = "year_of_experience")
    private int yearOfExperience;

    @Lob
    @Column(name = "profile_photo")
    private byte[] profilePhoto;

    @Column(name = "profile_summary", columnDefinition = "TEXT")
    private String profileSummary;

    @Lob
    private byte[] resume;
    
    @ManyToMany
    @JoinTable(
        name = "seeker_skills", 
        joinColumns = @JoinColumn(name = "jobseeker_id"), 
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skills> seekerSkills = new HashSet<>();
    
    @OneToMany(mappedBy = "jobSeeker")
    private List<Education> educations;
    
    @OneToMany(mappedBy = "jobSeeker")
    private List<Project> projects;
    
    @OneToMany(mappedBy = "jobSeeker")
    private List<Experience> experiences;
    
    @OneToMany(mappedBy = "jobSeeker")
    private List<Certification> certifications;
    
    @OneToOne(mappedBy = "jobSeeker")
    private JobPreference jobPreference;
    
    @ManyToMany
    @JoinTable(
        name = "saved_jobs", 
        joinColumns = @JoinColumn(name = "jobseeker_id"), 
        inverseJoinColumns = @JoinColumn(name = "job_id")
    )
    private Set<Jobs> savedJobs = new HashSet<>();
    
    @ManyToMany
    @JoinTable(
        name = "seeker_language", 
        joinColumns = @JoinColumn(name = "jobseeker_id"), 
        inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private Set<Language> seekerLanguages = new HashSet<>();
    
    @OneToMany(mappedBy = "jobSeeker")
    private List<JobApplication> jobApplications;
}
