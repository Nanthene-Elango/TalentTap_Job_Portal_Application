// com.talenttap.service.EmployerAuthService
package com.talenttap.service;

import com.talenttap.DTO.EmployerProfileDTO;
import com.talenttap.DTO.EmployerRegisterDTO;
import com.talenttap.entity.*;
import com.talenttap.exceptions.BadCredentialException;
import com.talenttap.exceptions.EmployerNotFoundException;
import com.talenttap.exceptions.InvalidCredentialsException;
import com.talenttap.repository.*;
import com.talenttap.security.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmployerAuthService {

    private final RoleRepository roleRepository;
    private final UsersRepository userRepository;
    private final LocationRepository locationRepository;
    private final IndustryTypeRepository industryTypeRepo;
    private final CompanyRepository companyRepository;
    private final EmployerRepository employerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtutil;
  

    public EmployerAuthService(RoleRepository roleRepository, UsersRepository userRepository,
                               LocationRepository locationRepository, IndustryTypeRepository industryTypeRepo,
                               CompanyRepository companyRepository, EmployerRepository employerRepository,
                               PasswordEncoder passwordEncoder, JwtUtil jwtutil) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.industryTypeRepo = industryTypeRepo;
        this.companyRepository = companyRepository;
        this.employerRepository = employerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtutil = jwtutil;
    }

    @Transactional
    public ResponseEntity<?> register(EmployerRegisterDTO dto) throws Exception {
        // Validate uniqueness
        if (userRepository.existsByUsername(dto.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        if (userRepository.existsByEmail(dto.getEmailId())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        if (companyRepository.existsByCompanyName(dto.getCompanyName())) {
            return ResponseEntity.badRequest().body("Company name already exists");
        }

        if (dto.getCompanyLogo() != null && dto.getCompanyLogo().getSize() > 5 * 1024 * 1024) {
            return ResponseEntity.badRequest().body("Logo file size exceeds 5MB");
        }

         Users user = new Users();
        user.setFullName(dto.getFullName());
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setMobileNumber(dto.getPhoneNumber());
        user.setEmail(dto.getEmailId());
        user.setRole(roleRepository.findByRole("EMPLOYER"));
        user.setAuthProvider(AuthProvider.MANUAL);
        user.setGoogleId(null);
        user.setDateOfRegistration(LocalDate.now());
        userRepository.save(user);

        // Create Company entity
        Company company = new Company();
        company.setAbout(dto.getAbout());
        company.setCompanyName(dto.getCompanyName());
        if (dto.getCompanyLogo() != null && !dto.getCompanyLogo().isEmpty()) {
            company.setCompanyLogo(dto.getCompanyLogo().getBytes());
        } else {
            company.setCompanyLogo(new byte[0]); // Default empty logo
        }
        company.setCompanySize(dto.getCompanySize());
        company.setLocation(locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new Exception("Location not found")));
        company.setEmail(dto.getCompanyEmail());
        company.setPhoneNumber(dto.getCompanyPhoneNumber());
        company.setVerified(false);
        company.setFoundedAt(dto.getFoundedAt());
        company.setIndustryType(industryTypeRepo.findById(dto.getIndustryType())
                .orElseThrow(() -> new Exception("Industry type not found")));
        company.setWebsiteUrl(dto.getWebUrl());
        companyRepository.save(company);

        // Create Employer entity
        Employer employer = new Employer();
        employer.setUser(user);
        employer.setCompany(company);
        employer.setDesignation(dto.getDesignation());
        employerRepository.save(employer);

        return ResponseEntity.ok("Employer Registered Successfully");
    }
    
    public EmployerProfileDTO profile(String jwt) {

        String username = jwtutil.extractIdentifier(jwt);
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Employer employer = employerRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        Company company = employer.getCompany();
        EmployerProfileDTO profileData = new EmployerProfileDTO();

        // Convert logo to base64
        String companyLogoBase64 = company.getCompanyLogo() != null && company.getCompanyLogo().length > 0
                ? Base64.getEncoder().encodeToString(company.getCompanyLogo())
                : null;

        // DTO Transfer data
        profileData.setFullname(user.getFullName());
        profileData.setUsername(user.getUsername());
        profileData.setPhoneNumber(user.getMobileNumber());
        profileData.setEmail(user.getEmail());
        profileData.setDesignation(employer.getDesignation());

        System.out.println("Data isbeign sent from service");
        return profileData;
    }

    public Map<String, Object> login(String input, String password, HttpServletResponse response) {
    	
        Users user;
        // Check if input is email
        if (input.contains("@")) {
            user = userRepository.findByEmail(input)
                    .orElseThrow(() -> new BadCredentialException("Invalid Email"));
        } else {
            user = userRepository.findByUsername(input)
                    .orElseThrow(() -> new BadCredentialException("Invalid Username"));
        }

        // Check if user is an employer
        Employer employer = employerRepository.findByUser(user)
                .orElseThrow(() -> new EmployerNotFoundException("Employer not found with this account"));

        // Check password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialException("Invalid Password!");
        }

        // Generate JWT
        String jwt = jwtutil.generateToken(user.getUsername(), "EMPLOYER");

        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setSecure(false);
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);
        System.out.println(jwt);
        Map<String, Object> response1 = new HashMap<>();
		response1.put("message", "Login successful");
		response1.put("code", 200);
		response1.put("timestamp", LocalDateTime.now());
       return response1;
    }

}