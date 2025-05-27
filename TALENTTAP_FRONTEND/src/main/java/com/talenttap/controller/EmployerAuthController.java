package com.talenttap.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.talenttap.DTO.EmailDTO;
import com.talenttap.DTO.EmployerProfileDTO;
import com.talenttap.DTO.EmployerRegisterDTO;
import com.talenttap.DTO.EditCompanyProfileDTO;
import com.talenttap.DTO.PasswordRequest;
import com.talenttap.model.EmployerRegister;
import com.talenttap.model.IndustryType;
import com.talenttap.model.JwtToken;
import com.talenttap.model.Location;
import com.talenttap.model.Login;
import com.talenttap.service.EmployerAuthService;
import com.talenttap.service.JobseekerService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class EmployerAuthController {

    private EmployerAuthService employerService;
    
    private JobseekerService jobseekerService;
    @Autowired
	private RestTemplate restTemplate;
	

    public EmployerAuthController(EmployerAuthService employerService, JobseekerService jobseekerService) {
        this.employerService = employerService;
        this.jobseekerService = jobseekerService;
    }

    @PostMapping("/employerRegister")
    public String processEmployerRegistration(
            @Valid @ModelAttribute("employerRegister") EmployerRegister employerRegister,
            BindingResult bindingResult,
            @RequestParam("companyLogo") MultipartFile companyLogo,
            Model model) {
        // Log form data for debugging
        System.out.println("Form Data - phoneNumber: " + employerRegister.getPhoneNumber());
        System.out.println("Form Data - emailId: " + employerRegister.getEmailId());
        System.out.println("Form Data - webUrl: " + employerRegister.getWebUrl());

        // Check for validation errors
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            model.addAttribute("error", "Please correct the following: " + errorMessage);
            model.addAttribute("companyIndustry", employerService.getAllIndustryType());
            return "employer/register";
        }

        try {
            // Validate companyLogo
            if (companyLogo.isEmpty()) {
                model.addAttribute("error", "Company logo is required.");
                model.addAttribute("companyIndustry", employerService.getAllIndustryType());
                return "employer/register";
            }

            // Validate file size (e.g., max 5MB)
            if (companyLogo.getSize() > 5 * 1024 * 1024) {
                model.addAttribute("error", "Company logo size must not exceed 5MB.");
               
                model.addAttribute("companyIndustry", employerService.getAllIndustryType());
                return "employer/register";
            }

            // Map to DTO
            EmployerRegisterDTO dto = new EmployerRegisterDTO();
            dto.setFullName(employerRegister.getFullName());
            dto.setUsername(employerRegister.getUsername());
            dto.setPassword(employerRegister.getPassword());
            dto.setPhoneNumber(employerRegister.getPhoneNumber());
            dto.setEmailId(employerRegister.getEmailId());
            dto.setCompanyName(employerRegister.getCompanyName());
            dto.setCompanyLogo(companyLogo);
            dto.setIndustryType(employerRegister.getIndustryType());
            dto.setCompanyEmail(employerRegister.getCompanyEmail());
            dto.setCompanyPhoneNumber(employerRegister.getCompanyPhoneNumber());
            dto.setCompanySize(employerRegister.getCompanySize());
            dto.setLocationId(employerRegister.getLocationId());
            dto.setWebUrl(employerRegister.getWebUrl());
            dto.setFoundedAt(employerRegister.getFoundedAt());
            dto.setAbout(employerRegister.getAbout());
            dto.setDesignation(employerRegister.getDesignation());

            // Log DTO for debugging
            System.out.println("DTO - phoneNumber: " + dto.getPhoneNumber());
            System.out.println("DTO - emailId: " + dto.getEmailId());
            System.out.println("DTO - webUrl: " + dto.getWebUrl());

            employerService.register(dto);
            return "redirect:/employer/login";
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            String errorMessage = e.getMessage().contains("400") ?
                    "Invalid data submitted: Please ensure phone number, email, and website URL are valid." :
                    "Registration failed: " + e.getMessage();
            model.addAttribute("error", errorMessage);
          
            return "employer/register";
        }
    }
    
    @PostMapping("/employer/Login")
    public String loginUser(@ModelAttribute("Login") Login login, BindingResult result, HttpServletResponse response, Model model) {
        boolean success = employerService.login(login, response, model);
        if (!success) {
            model.addAttribute("Login", login);
            return "employer/login";
        }
        return "redirect:/employer/employerDashboard";
    }
    
	@GetMapping("/employer/Logout")
	public String logout(HttpServletResponse response) {
	    Cookie cookie = new Cookie("jwt", null);
	    cookie.setPath("/");             
	    cookie.setHttpOnly(true);       
	    cookie.setMaxAge(0);            
	    response.addCookie(cookie);
	    return "redirect:/";     
	}
	
	
	@PostMapping("/employer/candidate/approve")
	public String handleApproveCandidate(@ModelAttribute EmailDTO email,
	                                     @RequestParam("applicantId") int applicantId,
	                                     @CookieValue(value = "jwt", required = false) String jwt,
	                                     RedirectAttributes redirectAttributes) {
		System.out.println("i reached the controlller");
	    if (jwt == null || jwt.isEmpty()) {
	        redirectAttributes.addFlashAttribute("error", "Unauthorized. Please log in.");
	        return "redirect:/employer/candidates";
	    }

	    try {
	        employerService.callApproveAPI(applicantId, email, jwt.trim());
	        redirectAttributes.addFlashAttribute("success", "Candidate approved successfully.");
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("error", "Error during approval: " + e.getMessage());
	    }

	    return "redirect:/employer/candidates";
	}
	
	@PostMapping("/employer/candidate/reject")
	public String handleRejectCandidate(@ModelAttribute EmailDTO email,
	                                     @RequestParam("applicantId") int applicantId,
	                                     @CookieValue(value = "jwt", required = false) String jwt,
	                                     RedirectAttributes redirectAttributes) {
		System.out.println("i reached the controlller");
	    if (jwt == null || jwt.isEmpty()) {
	        redirectAttributes.addFlashAttribute("error", "Unauthorized. Please log in.");
	        return "redirect:/employer/candidates";
	    }

	    try {
	        employerService.callRejectAPI(applicantId, email, jwt.trim());
	        redirectAttributes.addFlashAttribute("success", "Candidate rejected successfully.");
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("error", "Error during approval: " + e.getMessage());
	    }

	    return "redirect:/employer/candidates";
	}


    @PostMapping("/uploadIdentity")
    public String uploadIdentity(
            @RequestParam("govId") MultipartFile govIdFile,
            @RequestParam("companyId") MultipartFile companyIdFile,
            @RequestParam("companyEmail") String companyEmail,
            @CookieValue(value = "jwt", required = false) String jwt,
            Model model) {

        boolean uploadSuccess = employerService.handleIdentityUpload(govIdFile, companyIdFile, companyEmail, jwt);

        if (uploadSuccess) {
            model.addAttribute("message", "Files uploaded successfully!");
        } else {
            model.addAttribute("message", "Upload failed. Please try again.");
        }

        return "employer/employerProfile"; // or redirect to some page
    }
	
	 
	 @GetMapping("/employer/employer/profile/edit")
	 public String getCompanyProfile(@RequestParam(value = "edit", required = false,defaultValue = "false") Boolean edit, Model model, @CookieValue(value = "jwt", required = false) String jwt) {
			try {
				if (jwt != null && !jwt.trim().isEmpty()) {
					JwtToken token = new JwtToken(jwt.trim());
					EmployerProfileDTO profile = employerService.profile(token);
					System.out.println(
							"In employerProfile - Profile fetched: " + (profile != null ? profile.getFullname() : "null"));
					System.out.println(profile.getCompanyEmail());
					System.out.println(profile.getPhoneNumber());
					System.out.println(profile.getEmail());
					System.out.println(profile.getCompanyLogo());

					if (profile != null) {
						model.addAttribute("Fullname", profile.getFullname());
						model.addAttribute("username", profile.getUsername());
						model.addAttribute("email", profile.getEmail());
						model.addAttribute("phoneNumber", profile.getPhoneNumber());
						model.addAttribute("companyName", profile.getCompanyName());
						model.addAttribute("companyIndustry-1", profile.getIndustryType());
						model.addAttribute("companyEmail", profile.getCompanyEmail());
						model.addAttribute("companyPhone", profile.getCompanyPhone());
						model.addAttribute("designation", profile.getDesignation());
						model.addAttribute("companyAbout", profile.getAbout());
						model.addAttribute("companySize", profile.getCompanySize());
						model.addAttribute("location", profile.getLocation());
						model.addAttribute("companyLogo", profile.getCompanyLogo());
						model.addAttribute("loggedIn", true);
						model.addAttribute("editMode", edit != null && edit.equals("true"));
						List<Location> locations = jobseekerService.getAllLocations();
						System.out.println(locations.get(0).getLocation());
						model.addAttribute("locations", locations);

						List<IndustryType> industryType = employerService.getAllIndustryType();
						System.out.println(industryType.get(0).getIndustryType());
						model.addAttribute("companyIndustry", industryType);
						
						EditCompanyProfileDTO  dto = new EditCompanyProfileDTO();
						dto.setCompanyName(profile.getCompanyName());
						dto.setCompanyEmail(profile.getCompanyEmail());
						model.addAttribute("edit", dto);
					}
				} else {
					System.out.println("No JWT token found in cookie");
					model.addAttribute("loggedIn", false);
				}
			} catch (Exception e) {
				System.out.println("Error in employerProfile: " + e.getMessage());
				model.addAttribute("error", "Unable to load profile data. Please try again.");
				model.addAttribute("loggedIn", false);
			}
			return "employer/employerprofile";
	 }


  

	

	
	
	
}