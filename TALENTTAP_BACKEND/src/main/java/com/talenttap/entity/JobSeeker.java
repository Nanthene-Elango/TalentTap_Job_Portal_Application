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
    private Gender gender;

    @Column(name = "dob")
    private LocalDate dob;

    @ManyToOne
    @JoinColumn(name = "location")
    private Location location;

    @Column(name = "year_of_experience")
    private int yearOfExperience;

    @Lob
    @Column(name = "profile_photo" , columnDefinition = "LONGBLOB")
    private byte[] profilePhoto;

    @Column(name = "profile_summary", columnDefinition = "TEXT")
    private String profileSummary;

    @Lob
    @Column(name = "resume", columnDefinition = "LONGBLOB")
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

	public int getJobSeekerId() {
		return jobSeekerId;
	}

	public void setJobSeekerId(int jobSeekerId) {
		this.jobSeekerId = jobSeekerId;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public int getYearOfExperience() {
		return yearOfExperience;
	}

	public void setYearOfExperience(int yearOfExperience) {
		this.yearOfExperience = yearOfExperience;
	}

	public byte[] getProfilePhoto() {
		return profilePhoto;
	}

	public void setProfilePhoto(byte[] profilePhoto) {
		this.profilePhoto = profilePhoto;
	}

	public String getProfileSummary() {
		return profileSummary;
	}

	public void setProfileSummary(String profileSummary) {
		this.profileSummary = profileSummary;
	}

	public byte[] getResume() {
		return resume;
	}

	public void setResume(byte[] resume) {
		this.resume = resume;
	}

	public Set<Skills> getSeekerSkills() {
		return seekerSkills;
	}

	public void setSeekerSkills(Set<Skills> seekerSkills) {
		this.seekerSkills = seekerSkills;
	}

	public List<Education> getEducations() {
		return educations;
	}

	public void setEducations(List<Education> educations) {
		this.educations = educations;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public List<Experience> getExperiences() {
		return experiences;
	}

	public void setExperiences(List<Experience> experiences) {
		this.experiences = experiences;
	}

	public List<Certification> getCertifications() {
		return certifications;
	}

	public void setCertifications(List<Certification> certifications) {
		this.certifications = certifications;
	}

	public JobPreference getJobPreference() {
		return jobPreference;
	}

	public void setJobPreference(JobPreference jobPreference) {
		this.jobPreference = jobPreference;
	}

	public Set<Jobs> getSavedJobs() {
		return savedJobs;
	}

	public void setSavedJobs(Set<Jobs> savedJobs) {
		this.savedJobs = savedJobs;
	}

	public Set<Language> getSeekerLanguages() {
		return seekerLanguages;
	}

	public void setSeekerLanguages(Set<Language> seekerLanguages) {
		this.seekerLanguages = seekerLanguages;
	}

	public List<JobApplication> getJobApplications() {
		return jobApplications;
	}

	public void setJobApplications(List<JobApplication> jobApplications) {
		this.jobApplications = jobApplications;
	}
    
}
