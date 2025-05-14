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

    public ResponseEntity<?> loginAdmin(LoginDTO request, HttpServletResponse response) {
        Users user = usersRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
        if (!user.getRole().getRole().equals("ADMIN")) {
            return ResponseEntity.badRequest().body("Not an admin");
        }
        String jwt = jwtUtil.generateToken(user.getUsername(), user.getRole().getRole());
        System.out.print(jwt);
        Cookie jwtCookie = new Cookie("jwt", jwt);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(24 * 60 * 60);
        response.addCookie(jwtCookie);
        return ResponseEntity.ok("Admin login successful");
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