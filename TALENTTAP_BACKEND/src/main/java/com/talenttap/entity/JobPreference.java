package com.talenttap.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
public class JobPreference {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "job_preference_id")
	private int jobPreferenceId;
	
	@OneToOne
	@JoinColumn(name = "jobseeker_id" , nullable = false)
	private JobSeeker jobSeeker;
	
	@ManyToOne
	@JoinColumn(name = "employment_type_id")
	private EmploymentType employmentType;
	
	@OneToOne
	@JoinColumn(name = "preferred_salary_range" , referencedColumnName="salary_range_id")
	private SalaryRange preferredSalaryRange;
	
	@ManyToMany
    @JoinTable(
        name = "preferred_job_location", 
        joinColumns = @JoinColumn(name = "location_id"), 
        inverseJoinColumns = @JoinColumn(name = "job_preference_id")
    )
    private Set<Skills> preferredJobLocations = new HashSet<>();

	public int getJobPreferenceId() {
		return jobPreferenceId;
	}

	public void setJobPreferenceId(int jobPreferenceId) {
		this.jobPreferenceId = jobPreferenceId;
	}

	public JobSeeker getJobSeeker() {
		return jobSeeker;
	}

	public void setJobSeeker(JobSeeker jobSeeker) {
		this.jobSeeker = jobSeeker;
	}

	public EmploymentType getEmploymentType() {
		return employmentType;
	}

	public void setEmploymentType(EmploymentType employmentType) {
		this.employmentType = employmentType;
	}

	public SalaryRange getPreferredSalaryRange() {
		return preferredSalaryRange;
	}

	public void setPreferredSalaryRange(SalaryRange preferredSalaryRange) {
		this.preferredSalaryRange = preferredSalaryRange;
	}

	public Set<Skills> getPreferredJobLocations() {
		return preferredJobLocations;
	}

	public void setPreferredJobLocations(Set<Skills> preferredJobLocations) {
		this.preferredJobLocations = preferredJobLocations;
	}
	
}
