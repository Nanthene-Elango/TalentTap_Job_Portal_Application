package com.talenttap.exceptions;

import org.springframework.http.HttpStatus;

public class CompanyNotFoundException extends TalentTapException {
	public CompanyNotFoundException(String message) {
		super(message, HttpStatus.NOT_FOUND);
	}
}
