package com.talenttap.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.talenttap.model.CompanyProfile;
import com.talenttap.model.IndustryType;
import com.talenttap.model.Jobseeker;
import com.talenttap.model.Location;
import com.talenttap.service.CompanyService;
import com.talenttap.service.EmployerAuthService;
import com.talenttap.service.JobseekerService;

@Controller
@RequestMapping("employer/company")
public class CompanyController {

	private CompanyService companyService;

	private JobseekerService jobseekerService;

	private EmployerAuthService employerService;

	public CompanyController(CompanyService companyService, JobseekerService jobseekerService,
			EmployerAuthService employerService) {
		this.companyService = companyService;
		this.jobseekerService = jobseekerService;
		this.employerService = employerService;
	}

	@GetMapping("/profile")
	public String companyProfile(Model model, @CookieValue(value = "jwt", required = false) String jwt) {
		try {
			if (jwt != null && !jwt.trim().isEmpty()) {
				CompanyProfile profile = companyService.companyProfile(jwt);
				if (profile != null) {
					String logo = companyService.companyLogo(jwt);
					List<Location> locations = jobseekerService.getAllLocations();
					System.out.println(locations.get(0).getLocation());
					model.addAttribute("locations", locations);

			
					List<IndustryType> industryType = employerService.getAllIndustryType();
					System.out.println(industryType.get(0).getIndustryType());
					model.addAttribute("industry", industryType);
					model.addAttribute("companyProfile", profile);
					model.addAttribute("loggedIn", true);
					model.addAttribute("companyLogo", logo);

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
		return "employer/companyProfile";
	}

	@PostMapping("/update")
	public String updateCompanyProfile(@ModelAttribute CompanyProfile companyProfile,
			@CookieValue(value = "jwt", required = false) String jwt) {
		String message = companyService.updateProfile(companyProfile, jwt);
		return "redirect:/employer/company/profile";
	}

	@PostMapping("logo/update")
	public String updateCompanyLogo(@RequestParam("logoFile") MultipartFile logoFile,
			@CookieValue(value = "jwt", required = false) String jwt) {
		try {
			// Get the bytes
			byte[] logoBytes = logoFile.getBytes();

			String message = companyService.updateCompanyLogo(logoBytes, jwt);
			System.out.println(message);
			return "redirect:/employer/company/profile"; 
		} catch (IOException e) {
			e.printStackTrace();
			return "redirect:/employer/companyProfile"; 
		}
	}

}
