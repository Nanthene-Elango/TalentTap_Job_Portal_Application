package com.talenttap.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EducationLevel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int educationLevelId;
	
	@Column(nullable = false)
	private String educationLevel;
	
	@OneToMany(mappedBy = "educationLevel")
	private List<Education> educations;

	public int getEducationLevelId() {
		return educationLevelId;
	}

	public void setEducationLevelId(int educationLevelId) {
		this.educationLevelId = educationLevelId;
	}

	public String getEducationLevel() {
		return educationLevel;
	}

	public void setEducationLevel(String educationLevel) {
		this.educationLevel = educationLevel;
	}

	public List<Education> getEducations() {
		return educations;
	}

	public void setEducations(List<Education> educations) {
		this.educations = educations;
	}

}
