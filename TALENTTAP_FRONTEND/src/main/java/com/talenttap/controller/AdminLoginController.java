package com.talenttap.controller;

import com.talenttap.DTO.LoginDTO;
import com.talenttap.model.Login;
import com.talenttap.service.AdminService;
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

//    @GetMapping("/admin/login")
//    public String renderAdminLogin(Model model) {
//        model.addAttribute("login", new Login());
//        return "admin/admin-login";
//    }

    @PostMapping("/admin/login")
    public String processAdminLogin(@ModelAttribute("login") Login login, Model model) {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(login.getUsername());
        loginDTO.setPassword(login.getPassword());

        try {
            ResponseEntity<String> response = adminService.loginAdmin(loginDTO);
            if (response.getStatusCode().is2xxSuccessful() && "Admin login successful".equals(response.getBody())) {
                return "redirect:/admin/adminDashboard";
            } else {
                return "admin/admin-login";
            }
        } catch (Exception e) {
            return "admin/admin-login";
        }
    }
}