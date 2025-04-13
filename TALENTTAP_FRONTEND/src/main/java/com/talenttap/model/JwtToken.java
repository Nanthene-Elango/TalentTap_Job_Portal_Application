package com.talenttap.model;

public class JwtToken {

	private String jwt;
	
	public JwtToken(String jwt) {
		this.jwt = jwt;
	}

	public JwtToken() {

	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

}
