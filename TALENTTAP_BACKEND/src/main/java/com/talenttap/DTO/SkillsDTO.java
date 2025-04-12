package com.talenttap.DTO;

import com.talenttap.entity.Skills;

public class SkillsDTO {

	private int skillId;
	private String skill;
	
	public SkillsDTO() {
		super();
	}
	public SkillsDTO(Skills skill) {
		super();
		this.skillId = skill.getSkillId();
		this.skill = skill.getSkill();
	}
	public int getSkillId() {
		return skillId;
	}
	public void setSkillId(int skillID) {
		this.skillId = skillID;
	}
	public String getSkill() {
		return skill;
	}
	public void setSkill(String skill) {
		this.skill = skill;
	}
	
}
