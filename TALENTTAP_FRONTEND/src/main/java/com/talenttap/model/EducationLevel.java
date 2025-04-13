package com.talenttap.model;

public class EducationLevel {

	private int levelId;
	private String level;
	
	public EducationLevel() {
		super();
	}
	
	public EducationLevel(int id, String level) {
		super();
		this.levelId = id;
		this.level = level;
	}
	
	public int getLevelId() {
		return levelId;
	}
	public void setLevelId(int id) {
		this.levelId = id;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}

}
