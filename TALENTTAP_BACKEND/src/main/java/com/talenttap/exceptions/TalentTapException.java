package com.talenttap.exceptions;

import org.springframework.http.HttpStatus;

public class TalentTapException extends RuntimeException{
	private final HttpStatus status;
	
	public TalentTapException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}
	
	public HttpStatus getStatus() {
		return this.status;
	}
}
