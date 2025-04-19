package com.talenttap.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
public class Language {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "language_id")
	private int languageId;
	
	@Column(name = "language" , nullable = false , unique = true)
	private String language;
	
	@ManyToMany(mappedBy = "seekerLanguages")
	private Set<JobSeeker> seekers = new HashSet<>();

	public int getLanguageId() {
		return languageId;
	}

	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Set<JobSeeker> getSeekers() {
		return seekers;
	}

	public void setSeekers(Set<JobSeeker> seekers) {
		this.seekers = seekers;
	}	

}
