// com.talenttap.service.EmployerAuthService
package com.talenttap.service;

import com.talenttap.DTO.EmployerRegisterDTO;
import com.talenttap.entity.*;
import com.talenttap.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class EmployerAuthService {

    private final RoleRepository roleRepository;
    private final UsersRepository userRepository;
    private final LocationRepository locationRepository;
    private final IndustryTypeRepository industryTypeRepo;
    private final CompanyRepository companyRepository;
    private final EmployerRepository employerRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployerAuthService(RoleRepository roleRepository, UsersRepository userRepository,
                               LocationRepository locationRepository, IndustryTypeRepository industryTypeRepo,
                               CompanyRepository companyRepository, EmployerRepository employerRepository,
                               PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.industryTypeRepo = industryTypeRepo;
        this.companyRepository = companyRepository;
        this.employerRepository = employerRepository;
        this.passwordEncoder = passwordEncoder;
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
}