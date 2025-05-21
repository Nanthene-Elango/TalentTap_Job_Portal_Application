package com.talenttap.DTO;

import com.talenttap.entity.Language;

public class Languages {

	private Integer languageId;
	private String language;
	
	public Languages() {
		super();
	}

	public Languages(Language language) {
		super();
		this.languageId = language.getLanguageId();
		this.language = language.getLanguage();
	}
	
	public Integer getLanguageId() {
		return languageId;
	}
	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	
}
