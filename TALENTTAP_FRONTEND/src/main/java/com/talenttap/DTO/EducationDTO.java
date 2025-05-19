package com.talenttap.DTO;

public class EducationDTO {

	private Integer highestQualification;
    private String degree;
    private String institution;
    private Integer startYear;
    private Integer endYear;
    private String boardOfStudy;
    private Float percentage;
    
	public Integer getHighestQualification() {
		return highestQualification;
	}
	public void setHighestQualification(Integer highestQualification) {
		this.highestQualification = highestQualification;
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
	public Integer getStartYear() {
		return startYear;
	}
	public void setStartYear(Integer startYear) {
		this.startYear = startYear;
	}
	public Integer getEndYear() {
		return endYear;
	}
	public void setEndYear(Integer endYear) {
		this.endYear = endYear;
	}
	public String getBoardOfStudy() {
		return boardOfStudy;
	}
	public void setBoardOfStudy(String boardOfStudy) {
		this.boardOfStudy = boardOfStudy;
	}
	public Float getPercentage() {
		return percentage;
	}
	public void setPercentage(Float percentage) {
		this.percentage = percentage;
	}
    
}
