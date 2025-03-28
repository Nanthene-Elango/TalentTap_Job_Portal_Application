package com.talenttap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

	@GetMapping("/employer/login")
	public String loadEmployerLogin(Model model) {
		return "EmployerLogin";
	}
	
	@GetMapping("/jobseeker/login")
	public String loadJobSeekerLogin(Model model) {
		return "JobseekerLogin";
	}
	
	@GetMapping("/admin/login")
	public String loadAdminLogin(Model model) {
		return "AdminLogin";
	}
}
