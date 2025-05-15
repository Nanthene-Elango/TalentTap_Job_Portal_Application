package com.talenttap.exceptions;

import org.springframework.http.HttpStatus;

public class BadCredentialException extends TalentTapException {
	public BadCredentialException(String message) {
		super(message, HttpStatus.UNAUTHORIZED);
	}
}
  