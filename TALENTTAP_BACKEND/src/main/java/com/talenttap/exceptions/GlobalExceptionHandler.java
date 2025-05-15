package com.talenttap.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<Map<String , Object>> handerInvalidCredentialsException(InvalidCredentialsException ex){
		Map<String , Object> response = new HashMap<>();
		response.put("error" , ex);
		response.put("message", ex.getMessage());
		response.put("status" , HttpStatus.NOT_FOUND.value());
		
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(TalentTapException.class)
	public ResponseEntity<Map<String, Object>> handleTalentTapException(TalentTapException ex){
		Map<String, Object> response = new HashMap<>();
		response.put("message", ex.getMessage());
		response.put("code", ex.getStatus().value());
		response.put("timestamp", LocalDateTime.now());
		return ResponseEntity
	            .ok()
	            .contentType(MediaType.APPLICATION_JSON)
	            .body(response);
	}
}

