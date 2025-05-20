package com.talenttap.controller;

import com.talenttap.model.Login;
import com.talenttap.service.AdminService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminLoginController {

    private final AdminService adminService;

    public AdminLoginController(AdminService adminService) {
        this.adminService = adminService;
    }
//
//    @GetMapping("/admin/login")
//    public String renderAdminLogin(Model model) {
//        model.addAttribute("login", new Login());
//        return "admin/admin-login";
//    }

    @PostMapping("/admin/login")
    public String loginAdmin(@ModelAttribute("login") Login login,
                             HttpServletResponse response,
                             Model model) {
        String result = adminService.loginAdmin(login, response);
        
        if ("SUCCESS".equals(result)) {
            return "redirect:/admin/adminDashboard";
        }
        
        model.addAttribute("error", result);
        return "admin/admin-login";
    }
    
    @GetMapping("/admin/logout")
	public String logout(HttpServletResponse response) {
	    Cookie cookie = new Cookie("jwt", null);
	    cookie.setPath("/");             
	    cookie.setHttpOnly(true);       
	    cookie.setMaxAge(0);            
	    response.addCookie(cookie);
	    return "redirect:/admin/login";     
	}
    
}