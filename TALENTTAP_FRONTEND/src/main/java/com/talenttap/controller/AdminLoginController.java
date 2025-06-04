package com.talenttap.controller;

import com.talenttap.DTO.AdminProfileDTO;
import com.talenttap.DTO.ChangePasswordDTO;
import com.talenttap.model.ChangePasswordModel;
import com.talenttap.model.Login;
import com.talenttap.service.AdminService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        System.out.println("Login Result: " + response);
        if ("SUCCESS".equals(result)) {
            return "redirect:/admin/adminDashboard";
        }
        model.addAttribute("Login", login);
        model.addAttribute("error", result);
        return "redirect:/admin/login";
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
    
    @GetMapping("/admin/profile")
    public String showAdminProfile(Model model, @CookieValue(value = "jwt", required = false) String jwt) {
        if (jwt == null || jwt.trim().isEmpty()) {
            return "redirect:/admin/login";
        }
        try {
            AdminProfileDTO adminProfile = adminService.getAdminProfile(jwt);
            System.out.println(adminProfile.getFullName());
            model.addAttribute("admin", adminProfile);
            model.addAttribute("changePasswordModel", new ChangePasswordModel());
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load admin profile: " + e.getMessage());
        }
        return "admin/admin-profile";
    }

    @PostMapping("/admin/profile/change-password")
    public String changePassword(
            @ModelAttribute("changePasswordModel") ChangePasswordModel changePasswordModel,
            @CookieValue(value = "jwt", required = false) String jwt,
            RedirectAttributes redirectAttributes) {
        if (jwt == null || jwt.trim().isEmpty()) {
            return "redirect:/admin/admin-login";
        }
        try {
            ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
            changePasswordDTO.setOldPassword(changePasswordModel.getOldPassword());
            changePasswordDTO.setNewPassword(changePasswordModel.getNewPassword());
            changePasswordDTO.setConfirmNewPassword(changePasswordModel.getConfirmNewPassword());

            String result = adminService.changePassword(changePasswordDTO, jwt);
            redirectAttributes.addFlashAttribute("message", result);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to change password: " + e.getMessage());
        }
        return "redirect:/admin/profile";
    }
    
    
}