package com.talenttap.service;

import com.talenttap.DTO.AdminProfileDTO;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
            return ResponseEntity.badRequest().body(createErrorResponse("Invalid Password"));
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

    public AdminProfileDTO getAdminProfile(String jwt) {
        // Validate JWT and extract username
        String username = validateJwtAndGetUsername(jwt);

        // Fetch user from the database
        Optional<Users> userOptional = usersRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("Admin not found with username: " + username);
        }

        Users user = userOptional.get();

        // Verify the user is an admin
        if (!user.getRole().getRole().equals("ADMIN")) {
            throw new RuntimeException("User is not an admin");
        }
        
        // Map entity to DTO
        AdminProfileDTO adminProfileDTO = new AdminProfileDTO();
        adminProfileDTO.setFullName(user.getFullName());
        adminProfileDTO.setEmail(user.getEmail());
        adminProfileDTO.setUsername(user.getUsername());
        adminProfileDTO.setMobileNumber(user.getMobileNumber());
        adminProfileDTO.setDateOfRegistration(user.getDateOfRegistration());

        return adminProfileDTO;
    }

    // Update the admin's password
    public void changePassword(String oldPassword, String newPassword, String confirmNewPassword, String jwt) {
        // Validate JWT and extract username
        String username = validateJwtAndGetUsername(jwt);

        // Validate new password and confirmation match
        if (!newPassword.equals(confirmNewPassword)) {
            throw new RuntimeException("New password and confirmation do not match.");
        }

        // Fetch user from the database
        Optional<Users> userOptional = usersRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("Admin not found with username: " + username);
        }

        Users user = userOptional.get();

        System.out.println(user.getRole().getRole());
        // Verify the user is an admin
        if (!user.getRole().getRole().equals("ADMIN") && !user.getRole().getRole().equals("EMPLOYER")) {
            throw new RuntimeException("User is not an admin");
        }

        // Verify old password
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect.");
        }

        // Encode and update the new password
        user.setPassword(passwordEncoder.encode(newPassword));
        usersRepository.save(user);
    }

    // Helper method to validate JWT and extract username
    private String validateJwtAndGetUsername(String jwt) {
        try {
            String username = jwtUtil.extractIdentifier(jwt);
            
            if (username == null || !jwtUtil.validateToken(jwt)) {
                throw new RuntimeException("Invalid or expired JWT token");
            }
            return username;
        } catch (Exception e) {
            throw new RuntimeException("JWT validation failed: " + e.getMessage());
        }
    }
}