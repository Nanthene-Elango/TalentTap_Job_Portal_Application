package com.talenttap.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class Skills {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "skill_id")
	private int skillId;
	
	@Column(nullable = false , unique = true)
	private String skill;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "seekerSkills")
	private Set<JobSeeker> seekers = new HashSet<>();

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public Set<JobSeeker> getSeekers() {
		return seekers;
	}

	public void setSeekers(Set<JobSeeker> seekers) {
		this.seekers = seekers;
	}

}
