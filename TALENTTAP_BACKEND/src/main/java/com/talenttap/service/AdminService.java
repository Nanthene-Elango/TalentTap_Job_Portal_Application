package com.talenttap.service;

import com.talenttap.DTO.AdminRegisterDTO;
import com.talenttap.DTO.LoginDTO;
import com.talenttap.entity.Roles;
import com.talenttap.entity.Users;
import com.talenttap.repository.RoleRepository;
import com.talenttap.repository.UsersRepository;
import com.talenttap.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminService {


    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public ResponseEntity<Map<String, Object>> loginAdmin(LoginDTO request, HttpServletResponse response) {

        // Find user by username
    	  Users user = usersRepository.findByUsername(request.getUsername())
                  .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(createErrorResponse("Invalid credentials"));
        }

        // Check if user has ADMIN role
        if (!user.getRole().getRole().equals("ADMIN")) {
            return ResponseEntity.badRequest().body(createErrorResponse("Not an admin"));
        }

        // Generate JWT token
        String jwt = jwtUtil.generateToken(user.getUsername(), user.getRole().getRole());
        System.out.print(jwt);        // Set JWT in a secure cookie
        Cookie jwtCookie = new Cookie("jwt", jwt);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setSecure(false);
        jwtCookie.setMaxAge(24 * 60 * 60);// Prevent CSRF
        response.addCookie(jwtCookie);

        // Return success response
        Map<String, Object> successResponse = new HashMap<>();
        successResponse.put("message", "Admin login successful");
        successResponse.put("username", user.getUsername());
        successResponse.put("jwt",jwt);
        return ResponseEntity.ok(successResponse);
    }

    // Helper method to create error response
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", message);
        return errorResponse;
    }
    
    public ResponseEntity<?> registerAdmin(AdminRegisterDTO request) {
        if (usersRepository.existsByUsername(request.getUsername()) || 
            usersRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Username or email already exists");
        }

        Roles adminRole = roleRepository.findByRole("ADMIN");
        if (adminRole == null) {
            adminRole = new Roles();
            adminRole.setRole("ADMIN");
            roleRepository.save(adminRole);
        }

        Users admin = new Users();
        admin.setUsername(request.getUsername());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setEmail(request.getEmail());
        admin.setMobileNumber(request.getMobileNumber());
        admin.setFullName(request.getFullName());
        admin.setRole(adminRole);
        admin.setDateOfRegistration(LocalDate.now());

        usersRepository.save(admin);
        return ResponseEntity.ok("Admin registered successfully");
    }

}