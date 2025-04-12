package com.talenttap.DTO;

import com.talenttap.entity.EducationLevel;

public class EducationLevelDTO {

	private int levelId;
	private String level;
	
	public EducationLevelDTO(EducationLevel level) {
		this.level = level.getEducationLevel();
		this.levelId = level.getEducationLevelId();
	}
	
	public int getLevelId() {
		return levelId;
	}
	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
}
