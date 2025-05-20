package com.talenttap.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


//@ControllerAdvice
//public class GlobalExceptionHandler {
//	
//	@ExceptionHandler(TalentTapException.class)
//	public ResponseEntity<Map<String, Object>> handleTalentTapException(TalentTapException ex){
//		Map<String, Object> response = new HashMap<>();
//		response.put("message", ex.getMessage());
//		response.put("code", ex.getStatus().value());
//		response.put("timestamp", LocalDateTime.now());
//		return ResponseEntity
//	            .ok()
//	            .contentType(MediaType.APPLICATION_JSON)
//	            .body(response);
//	}
//}


