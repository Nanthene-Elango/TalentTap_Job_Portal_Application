package com.talenttap.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "project_id")
	private int projectId;

	@ManyToOne
	@JoinColumn(name = "job_seeker_id", nullable = false)
	private JobSeeker jobSeeker;
	
	@Column(name = "project_title" , nullable = false)
	private String projectTitle;
	
	@Column(name = "description" , columnDefinition = "TEXT")
	private String description;
	
	@Column(name = "project_url")
	private String projectURL;
	
}
