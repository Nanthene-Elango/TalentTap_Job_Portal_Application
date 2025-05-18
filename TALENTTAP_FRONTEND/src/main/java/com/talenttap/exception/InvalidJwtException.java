package com.talenttap.exception;

public class InvalidJwtException extends TalentTapException {
	public InvalidJwtException(String message) {
		super(message, 401);
	}
}
