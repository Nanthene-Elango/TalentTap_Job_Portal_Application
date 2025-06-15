package com.talenttap.service;

import com.talenttap.DTO.AdminProfileDTO;
import com.talenttap.DTO.AdminRegisterDTO;
import com.talenttap.DTO.EmployerAdminDTO;
import com.talenttap.DTO.EmployerDetailsDTO;
import com.talenttap.DTO.JobSeekerAdminDTO;
import com.talenttap.DTO.JobSeekerDetailsDTO;
import com.talenttap.DTO.LoginDTO;
import com.talenttap.entity.Employer;
import com.talenttap.entity.JobSeeker;
import com.talenttap.entity.Jobs;
import com.talenttap.entity.Roles;
import com.talenttap.entity.Skills;
import com.talenttap.entity.Users;
import com.talenttap.repository.EmployerRepository;
import com.talenttap.repository.JobApplicationRepository;
import com.talenttap.repository.JobsRepository;
import com.talenttap.repository.JobseekerRepository;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminService {


    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmployerRepository employerRepository;
    
    @Autowired
    private JobsRepository jobsRepository;
    
    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private JobseekerRepository jobSeekerRepository;
    
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
    
    public List<EmployerAdminDTO> getAllEmployers(String jwt) {
        // Validate JWT
        if (jwt == null || jwt.isBlank()) {
            throw new IllegalArgumentException("JWT token is empty or null");
        }

        // Extract username and validate user
        String username = jwtUtil.extractIdentifier(jwt);
        Users user = usersRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found: " + username));

        // Check if user is admin
        if (!"ADMIN".equals(user.getRole().getRole())) {
            throw new RuntimeException("User is not authorized as admin");
        }

        List<Employer> employers = employerRepository.findAll();
        List<EmployerAdminDTO> employerDTOs = new ArrayList<>();

        for (Employer employer : employers) {
            EmployerAdminDTO dto = new EmployerAdminDTO();
            dto.setEmployerId(employer.getEmployerId());
            dto.setCompanyName(employer.getCompany().getCompanyName());
            dto.setCompanyEmail(employer.getCompany().getEmail());
            dto.setIndustryType(employer.getCompany().getIndustryType().getIndustryType());
            dto.setRegisteredDate(employer.getUser().getDateOfRegistration());
            dto.setStatus(employer.getCompany().isVerified() ? "Active" : "Inactive");

            // Fetch job count
            List<Jobs> jobs = jobsRepository.findByEmployer(employer);
            dto.setJobCount(jobs.size());

            // Fetch application count
            int applicationCount = jobApplicationRepository.findByJob_Employer(employer).size();
            dto.setApplicationCount(applicationCount);

            employerDTOs.add(dto);
        }

        return employerDTOs;
    }

    public List<EmployerAdminDTO> searchEmployers(String jwt, String statusFilter, String industryFilter, 
                                                 String searchTerm, LocalDate startDate, LocalDate endDate) {
        // Validate JWT
        if (jwt == null || jwt.isBlank()) {
            throw new IllegalArgumentException("JWT token is empty or null");
        }

        // Extract username and validate user
        String username = jwtUtil.extractIdentifier(jwt);
        Users user = usersRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found: " + username));

        // Check if user is admin
        if (!"ADMIN".equals(user.getRole().getRole())) {
            throw new RuntimeException("User is not authorized as admin");
        }

        List<Employer> employers = employerRepository.findAll();
        List<EmployerAdminDTO> employerDTOs = new ArrayList<>();

        for (Employer employer : employers) {
            // Apply filters
            boolean matchesFilter = true;

            // Status filter
            if (statusFilter != null && !statusFilter.isEmpty()) {
                boolean isVerified = employer.getCompany().isVerified();
                String employerStatus = isVerified ? "active" : "inactive";
                if (!statusFilter.equalsIgnoreCase(employerStatus)) {
                    matchesFilter = false;
                }
            }

            // Industry filter
            if (industryFilter != null && !industryFilter.isEmpty()) {
                String industryType = employer.getCompany().getIndustryType().getIndustryType().toLowerCase();
                if (!industryType.contains(industryFilter.toLowerCase())) {
                    matchesFilter = false;
                }
            }

            // Search term (company name or email)
            if (searchTerm != null && !searchTerm.isEmpty()) {
                String searchLower = searchTerm.toLowerCase();
                boolean matchesSearch = employer.getCompany().getCompanyName().toLowerCase().contains(searchLower) ||
                                       employer.getCompany().getEmail().toLowerCase().contains(searchLower);
                if (!matchesSearch) {
                    matchesFilter = false;
                }
            }

            // Date range filter
            if (startDate != null && employer.getUser().getDateOfRegistration().isBefore(startDate)) {
                matchesFilter = false;
            }
            if (endDate != null && employer.getUser().getDateOfRegistration().isAfter(endDate)) {
                matchesFilter = false;
            }

            if (matchesFilter) {
                EmployerAdminDTO dto = new EmployerAdminDTO();
                dto.setEmployerId(employer.getEmployerId());
                dto.setCompanyName(employer.getCompany().getCompanyName());
                dto.setCompanyEmail(employer.getCompany().getEmail());
                dto.setIndustryType(employer.getCompany().getIndustryType().getIndustryType());
                dto.setRegisteredDate(employer.getUser().getDateOfRegistration());
                dto.setStatus(employer.getCompany().isVerified() ? "Active" : "Inactive");

                // Fetch job count
                List<Jobs> jobs = jobsRepository.findByEmployer(employer);
                dto.setJobCount(jobs.size());

                // Fetch application count
                int applicationCount = jobApplicationRepository.findByJob_Employer(employer).size();
                dto.setApplicationCount(applicationCount);

                employerDTOs.add(dto);
            }
        }

        return employerDTOs;
    }

    public EmployerDetailsDTO getEmployerDetails(int employerId, String jwt) {
    	// Validate JWT
    	if (jwt == null || jwt.isBlank()) {
    		throw new IllegalArgumentException("JWT token is empty or null");
    	}

    	// Extract username and validate user
    	String username = jwtUtil.extractIdentifier(jwt);
    	Users user = usersRepository.findByUsername(username)
    			.orElseThrow(() -> new RuntimeException("User not found: " + username));

    	Employer employer = employerRepository.findById(employerId)
    			.orElseThrow(() -> new RuntimeException("Employer not found with ID: " + employerId));

    	EmployerDetailsDTO dto = new EmployerDetailsDTO();
    	dto.setEmployerId(employer.getEmployerId());
    	dto.setFullName(employer.getUser().getFullName());
    	dto.setUsername(employer.getUser().getUsername());
    	dto.setEmail(employer.getUser().getEmail());
    	dto.setMobileNumber(employer.getUser().getMobileNumber());
    	dto.setDesignation(employer.getDesignation());
    	dto.setDateOfRegistration(employer.getUser().getDateOfRegistration());
    	dto.setCompanyName(employer.getCompany().getCompanyName());
    	dto.setCompanyEmail(employer.getCompany().getEmail());
    	dto.setCompanyPhone(employer.getCompany().getPhoneNumber());
    	dto.setCompanySize(employer.getCompany().getCompanySize());
    	dto.setLocation(employer.getCompany().getLocation().getLocation());
    	dto.setWebsiteUrl(employer.getCompany().getWebsiteUrl());
    	dto.setFoundedAt(employer.getCompany().getFoundedAt());
    	dto.setAbout(employer.getCompany().getAbout());
    	dto.setIndustryType(employer.getCompany().getIndustryType().getIndustryType());
    	dto.setVerified(employer.getCompany().isVerified());

    	return dto;
    }

    public void updateVerificationStatus(List<Integer> employerIds, boolean isVerified, String jwt) {
    	// Validate JWT
    	if (jwt == null || jwt.isBlank()) {
    		throw new IllegalArgumentException("JWT token is empty or null");
    	}

    	// Extract username and validate user
    	String username = jwtUtil.extractIdentifier(jwt);
    	Users user = usersRepository.findByUsername(username)
    			.orElseThrow(() -> new RuntimeException("User not found: " + username));

    	if (employerIds == null || employerIds.isEmpty()) {
    		throw new IllegalArgumentException("Employer IDs list cannot be empty");
    	}

    	for (Integer employerId : employerIds) {
    		Employer employer = employerRepository.findById(employerId)
    				.orElseThrow(() -> new RuntimeException("Employer not found with ID: " + employerId));
    		employer.getCompany().setVerified(isVerified);
    		employerRepository.save(employer);
    	}
    }
    
    public List<JobSeekerAdminDTO> getAllJobSeekers(String jwt) {
        if (jwt == null || jwt.isBlank()) {
            throw new IllegalArgumentException("JWT token is empty or null");
        }

        String username = jwtUtil.extractIdentifier(jwt);
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        if (!"ADMIN".equals(user.getRole().getRole())) {
            throw new RuntimeException("User is not authorized as admin");
        }

        List<JobSeeker> jobSeekers = jobSeekerRepository.findAll();
        List<JobSeekerAdminDTO> jobSeekerDTOs = new ArrayList<>();

        for (JobSeeker jobSeeker : jobSeekers) {
            JobSeekerAdminDTO dto = new JobSeekerAdminDTO();
            dto.setJobSeekerId(jobSeeker.getJobSeekerId());
            dto.setName(jobSeeker.getUser().getFullName());
            dto.setEmail(jobSeeker.getUser().getEmail());
            dto.setLocation(jobSeeker.getLocation() != null ? jobSeeker.getLocation().getLocation() : "N/A");
            dto.setExperience(jobSeeker.getYearOfExperience());
            dto.setRegistered(jobSeeker.getUser().getDateOfRegistration());

            // Use JobApplicationRepository to count applications
            int applicationCount = jobApplicationRepository.findByJobSeeker(jobSeeker).size();
            dto.setApplicationCount(applicationCount);

            jobSeekerDTOs.add(dto);
        }

        return jobSeekerDTOs;
    }

    public JobSeekerDetailsDTO getJobSeekerDetails(int jobSeekerId, String jwt) {
        if (jwt == null || jwt.isBlank()) {
            throw new IllegalArgumentException("JWT token is empty or null");
        }

        String username = jwtUtil.extractIdentifier(jwt);
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        if (!"ADMIN".equals(user.getRole().getRole())) {
            throw new RuntimeException("User is not authorized as admin");
        }

        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId)
                .orElseThrow(() -> new RuntimeException("JobSeeker not found with ID: " + jobSeekerId));

        JobSeekerDetailsDTO dto = new JobSeekerDetailsDTO();
        dto.setJobSeekerId(jobSeeker.getJobSeekerId());
        String fullName = jobSeeker.getUser().getFullName();
        dto.setFullName(fullName);

        // Split fullName into firstName and lastName
        String[] nameParts = fullName != null ? fullName.split("\\s+", 2) : new String[]{"", ""};
        dto.setFirstName(nameParts.length > 0 ? nameParts[0] : "");
        dto.setLastName(nameParts.length > 1 ? nameParts[1] : "");

        dto.setEmail(jobSeeker.getUser().getEmail());
        dto.setPhone(jobSeeker.getUser().getMobileNumber());
        dto.setSkills(jobSeeker.getSeekerSkills().stream()
                .map(Skills::getSkill)
                .collect(Collectors.toList()));
        dto.setResumeBase64(jobSeeker.getResume() != null ? Base64.getEncoder().encodeToString(jobSeeker.getResume()) : null);
        dto.setRegistrationDate(jobSeeker.getUser().getDateOfRegistration());
        dto.setLocation(jobSeeker.getLocation() != null ? jobSeeker.getLocation().getLocation() : "N/A");

        // Use JobApplicationRepository to count applications
        int applicationCount = jobApplicationRepository.findByJobSeeker(jobSeeker).size();
        dto.setApplicationCount(applicationCount);

        return dto;
    }
}