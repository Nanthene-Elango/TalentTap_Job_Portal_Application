package com.talenttap.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidJwtException extends TalentTapException {
	public InvalidJwtException(String message) {
		super(message, HttpStatus.UNAUTHORIZED);
	}
}
