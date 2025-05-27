package com.talenttap.DTO;

import com.talenttap.entity.Project;

public class ProjectDTO {

	private Integer id;
	private String title;
	private String description;
	private String url;
	
	public ProjectDTO() {
		super();
	}
	
	public ProjectDTO(Project project) {
		this.id = project.getProjectId();
		this.title = project.getProjectTitle();
		this.description = project.getDescription();
		this.url = project.getProjectURL();
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
