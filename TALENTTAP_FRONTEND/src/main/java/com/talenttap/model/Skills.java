package com.talenttap.model;

public class Skills {

	private int skillId;
	private String skill;
	
	public Skills() {
		super();
	}
	
	public Skills(int skillId, String skill) {
		super();
		this.skillId = skillId;
		this.skill = skill;
	}
	
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
	
}
