package com.talenttap.exceptions;

import org.springframework.http.HttpStatus;

public class EmployerNotFoundException extends TalentTapException {
	public EmployerNotFoundException(String message) {
		super(message, HttpStatus.NOT_FOUND);
	}
}
