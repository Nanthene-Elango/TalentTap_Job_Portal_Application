package com.talenttap.DTO;

import com.talenttap.entity.Education;

public class EducationDTO {

	private int educationId;
    private String educationLevel;
    private String degree;
    private String institution;
    private String boardOfStudy;
    private float percentage;
    private int startYear;
    private int endYear;
    
    public EducationDTO() {
		super();
	}

	public EducationDTO(Education education) {
    	this.educationId = education.getEducationId();
    	this.boardOfStudy = education.getBoardOfStudy();
    	this.degree = education.getDegree();
    	this.educationLevel = education.getEducationLevel().getEducationLevel();
    	this.endYear = education.getEndYear();
    	this.startYear = education.getStartYear();
    	this.institution = education.getInstitution();
    	this.percentage = education.getPercentage();
    }
    
	public int getEducationId() {
		return educationId;
	}
	public void setEducationId(int educationId) {
		this.educationId = educationId;
	}
	public String getEducationLevel() {
		return educationLevel;
	}
	public void setEducationLevel(String educationLevel) {
		this.educationLevel = educationLevel;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getInstitution() {
		return institution;
	}
	public void setInstitution(String institution) {
		this.institution = institution;
	}
	public String getBoardOfStudy() {
		return boardOfStudy;
	}
	public void setBoardOfStudy(String boardOfStudy) {
		this.boardOfStudy = boardOfStudy;
	}
	public float getPercentage() {
		return percentage;
	}
	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}
	public int getStartYear() {
		return startYear;
	}
	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}
	public int getEndYear() {
		return endYear;
	}
	public void setEndYear(int endYear) {
		this.endYear = endYear;
	}
}
