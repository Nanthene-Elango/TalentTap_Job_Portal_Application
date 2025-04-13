package com.talenttap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GoogleAuthController {

	@GetMapping("/oauth2/authorization/google")
	public void googleSignIn() {
		
	}
	
}
