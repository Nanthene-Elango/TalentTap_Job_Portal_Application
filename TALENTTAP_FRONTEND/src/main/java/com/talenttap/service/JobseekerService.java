package com.talenttap.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.talenttap.DTO.EducationDTO;
import com.talenttap.model.Certifications;
import com.talenttap.model.Education;
import com.talenttap.model.EducationLevel;
import com.talenttap.model.Experience;
import com.talenttap.model.JobFilter;
import com.talenttap.model.Jobs;
import com.talenttap.model.Jobseeker;
import com.talenttap.model.JobseekerRegister;
import com.talenttap.model.JwtToken;
import com.talenttap.model.Languages;
import com.talenttap.model.Location;
import com.talenttap.model.Login;
import com.talenttap.model.Projects;
import com.talenttap.model.Skills;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class JobseekerService {

	@Autowired
	private RestTemplate restTemplate;

	public List<Location> getAllLocations() {

		String url = "http://localhost:8083/api/locations";

		ResponseEntity<Location[]> response = restTemplate.getForEntity(url, Location[].class);
		Location[] locationArray = response.getBody();

		List<Location> locations = Arrays.asList(locationArray);

		return locations;
	}

	public List<Skills> getAllSkills() {

		String url = "http://localhost:8083/api/skills";

		ResponseEntity<Skills[]> response = restTemplate.getForEntity(url, Skills[].class);
		Skills[] skillsArray = response.getBody();

		List<Skills> skills = Arrays.asList(skillsArray);

		return skills;
	}

	public List<EducationLevel> getEducationLevel() {
		String url = "http://localhost:8083/api/educationlevel";

		ResponseEntity<EducationLevel[]> response = restTemplate.getForEntity(url, EducationLevel[].class);
		EducationLevel[] educationLevel = response.getBody();

		return Arrays.asList(educationLevel);
	}

	public void register(JobseekerRegister jobseekerRegister) {

		String url = "http://localhost:8083/auth/register/jobseeker";

		restTemplate.postForEntity(url, jobseekerRegister, String.class);
	}

	public void login(Login login, HttpServletResponse response) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Login> request = new HttpEntity<>(login, headers);

		ResponseEntity<String> backendResponse = restTemplate.exchange("http://localhost:8083/auth/login/jobseeker",
				HttpMethod.POST, request, String.class);
		List<String> cookieHeaders = backendResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
		if (cookieHeaders != null) {
			for (String cookieHeader : cookieHeaders) {
				response.addHeader(HttpHeaders.SET_COOKIE, cookieHeader);
			}
		}

		System.out.println(backendResponse.getBody());
	}

	public String getFullName(JwtToken jwtToken) {

		String url = "http://localhost:8083/jobseeker/fullName";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<JwtToken> request = new HttpEntity<>(jwtToken, headers);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			return response.getBody();
		} else {
			return null;
		}
	}

	public Jobseeker getJobseeker(JwtToken jwt) {
		String url = "http://localhost:8083/jobseeker";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<JwtToken> request = new HttpEntity<>(jwt, headers);

		ResponseEntity<Jobseeker> response = restTemplate.exchange(url, HttpMethod.POST, request, Jobseeker.class);

		return response.getBody();
	}

	public void updateProfilePicture(MultipartFile file, Integer jobSeekerId) {

		try {

			RestTemplate restTemplate = new RestTemplate();

			String backendUrl = "http://localhost:8083/jobseeker/upload-profile-photo";

			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("profilePhoto", new MultipartInputStreamFileResource(file.getInputStream(),
					file.getOriginalFilename(), file.getSize()));
			body.add("jobSeekerId", jobSeekerId);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

			// Send the POST request
			ResponseEntity<String> response = restTemplate.postForEntity(backendUrl, requestEntity, String.class);

			if (response.getStatusCode().is2xxSuccessful()) {
				// Success
			} else {
				// Handle failure case
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateProfile(Jobseeker jobSeeker) {
		String url = "http://localhost:8083/jobseeker/update-profile";
		restTemplate.put(url, jobSeeker);
	}

	public void updateSummary(String summary, int id) {

		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put("summary", summary);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestMap, headers);

		ResponseEntity<String> response = restTemplate.exchange("http://localhost:8083/jobseeker/update-summary/" + id,
				HttpMethod.PUT, requestEntity, String.class);

		System.out.println(response.getBody());

	}

	public Jobs getJobById(int id) {
		String url = "http://localhost:8083/api/job/" + id;

		ResponseEntity<Jobs> response = restTemplate.getForEntity(url, Jobs.class);

		return response.getBody();
	}

	public List<Jobs> filterJobs(JobFilter jobFilter) {

		String url = "http://localhost:8083/api/jobs/filter";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<JobFilter> requestEntity = new HttpEntity<>(jobFilter, headers);

		ResponseEntity<Jobs[]> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Jobs[].class);

		return Arrays.asList(response.getBody());
	}

	public List<Education> getAllEducation(int id) {

		String url = "http://localhost:8083/jobseeker/educations/" + id;

		ResponseEntity<Education[]> response = restTemplate.getForEntity(url, Education[].class);

		return Arrays.asList(response.getBody());
	}

	public List<Skills> getAllSkillsById(int id) {

		String url = "http://localhost:8083/jobseeker/skills/" + id;

		ResponseEntity<Skills[]> response = restTemplate.getForEntity(url, Skills[].class);

		return Arrays.asList(response.getBody());
	}

	public void deleteSkillById(Long id, Long jobseekerId) {
		String url = "http://localhost:8083/jobseeker/delete/skill/" + id + "/" + jobseekerId;

		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String.class);

	}

	public String handleResumeUpload(MultipartFile file, String jwt, RedirectAttributes redirectAttributes) {

		if (file.isEmpty() || jwt == null || jwt.trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Invalid request");
			return "redirect:/profile";
		}

		try {
			// Create headers with JWT cookie
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.add("Cookie", "jwt=" + jwt);

			// Convert file to resource
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename(),
					file.getSize()));

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

			String url = "http://localhost:8083/jobseeker/resume/upload";
			ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

			redirectAttributes.addFlashAttribute("message", response.getBody());

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Upload failed: " + e.getMessage());
		}

		return "redirect:/profile";
	}

	public ResponseEntity<byte[]> downloadResume(String jwt) {
		if (jwt == null || jwt.trim().isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
			headers.add("Cookie", "jwt=" + jwt.trim());

			HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

			String url = "http://localhost:8083/jobseeker/resume";

			ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, byte[].class);

			if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
				return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"resume.pdf\"")
						.body(response.getBody());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return ResponseEntity.notFound().build();
	}

	public ResponseEntity<byte[]> previewResume(String jwt) {
		if (jwt == null || jwt.trim().isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_PDF));
			headers.add("Cookie", "jwt=" + jwt.trim());

			HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

			String url = "http://localhost:8083/jobseeker/resume";

			ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, byte[].class);

			if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
				return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(response.getBody());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.notFound().build();
	}

	public String deleteResume(String jwt, RedirectAttributes redirectAttributes) {
		if (jwt != null && !jwt.trim().isEmpty()) {
			try {
				HttpHeaders headers = new HttpHeaders();
				headers.add("Cookie", "jwt=" + jwt.trim());

				HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

				String url = "http://localhost:8083/jobseeker/resume/delete";

				restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class);
				redirectAttributes.addFlashAttribute("success", "Resume removed successfully.");

			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("error", "Failed to remove resume.");
				e.printStackTrace();
			}
		}
		return "redirect:/profile";
	}

	public void applyJob(String jwt, int id) {
		if (jwt != null && !jwt.trim().isEmpty()) {
			try {
				HttpHeaders headers = new HttpHeaders();
				headers.add("Cookie", "jwt=" + jwt.trim());

				HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

				String url = "http://localhost:8083/jobseeker/job/apply/" + id;

				restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Failed to apply for job");
			}
		} else {
			throw new RuntimeException("JWT token not found");
		}
	}

	public boolean hasApplied(String jwt, int id) {
		if (jwt != null && !jwt.trim().isEmpty()) {
			try {
				HttpHeaders headers = new HttpHeaders();
				headers.add("Cookie", "jwt=" + jwt.trim());

				HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

				String url = "http://localhost:8083/jobseeker/job/is-applied/" + id;

				ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
						Boolean.class);

				return Boolean.TRUE.equals(response.getBody());
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	public String addEducation(int id, EducationDTO education, RedirectAttributes redirectAttributes) {

		String url = "http://localhost:8083/jobseeker/education/add/" + id;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<EducationDTO> requestEntity = new HttpEntity<>(education, headers);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			redirectAttributes.addFlashAttribute("success", "education added successfully!");
		} else {
			redirectAttributes.addFlashAttribute("error", "error adding education!");
		}
		return "redirect:/profile";
	}

	public String editEducation(Education education, RedirectAttributes redirectAttributes) {
		String url = "http://localhost:8083/jobseeker/education/update/" + education.getEducationId();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Education> requestEntity = new HttpEntity<>(education, headers);

		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);

			if (response.getStatusCode().is2xxSuccessful()) {
				redirectAttributes.addFlashAttribute("success", "Education updated successfully!");
			} else {
				redirectAttributes.addFlashAttribute("error", "Failed to update education.");
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error while updating education" + e.getMessage());
		}
		return "redirect:/profile";
	}

	public String deleteEducation(int id, RedirectAttributes redirectAttributes) {

		String url = "http://localhost:8083/jobseeker/education/delete/" + id;

		try {
			restTemplate.delete(url);
			redirectAttributes.addFlashAttribute("success", "education deleted successfully!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error occured while deleting education!");
		}

		return "redirect:/profile";
	}

	public String addSkills(Long jobSeekerId, List<Long> skillIds, RedirectAttributes redirectAttributes) {

		String url = "http://localhost:8083/jobseeker/skill/add/" + jobSeekerId;

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
			for (Long skillId : skillIds) {
				body.add("skillIds", skillId.toString());
			}

			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

			restTemplate.postForEntity(url, request, String.class);

			redirectAttributes.addFlashAttribute("success", "Skills added successfully.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Failed to add skills: " + e.getMessage());
		}

		return "redirect:/profile";
	}

	public List<Certifications> getAllCertifications(int id) {

		String url = "http://localhost:8083/jobseeker/certifications/" + id;

		ResponseEntity<Certifications[]> response = restTemplate.getForEntity(url, Certifications[].class);

		return Arrays.asList(response.getBody());
	}

	public String addCertification(int id, Certifications certification, RedirectAttributes redirectAttributes) {

		String url = "http://localhost:8083/jobseeker/certification/add/" + id;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Certifications> requestEntity = new HttpEntity<>(certification, headers);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			redirectAttributes.addFlashAttribute("success", "certification added successfully!");
		} else {
			redirectAttributes.addFlashAttribute("error", "error adding certification!");
		}
		return "redirect:/profile";
	}

	public String editCertification(Certifications certification, RedirectAttributes redirectAttributes) {
		String url = "http://localhost:8083/jobseeker/certification/update/" + certification.getId();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Certifications> requestEntity = new HttpEntity<>(certification, headers);

		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);

			if (response.getStatusCode().is2xxSuccessful()) {
				redirectAttributes.addFlashAttribute("success", "Certificate updated successfully!");
			} else {
				redirectAttributes.addFlashAttribute("error", "Failed to update Certificate.");
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error while updating certificate" + e.getMessage());
		}
		return "redirect:/profile";
	}

	public String deleteCertification(int id, RedirectAttributes redirectAttributes) {
		String url = "http://localhost:8083/jobseeker/certification/delete/" + id;

		try {
			restTemplate.delete(url);
			redirectAttributes.addFlashAttribute("success", "certification deleted successfully!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error occured while deleting certification!");
		}

		return "redirect:/profile";
	}

	public List<Languages> getAllLanguages() {
		
		String url = "http://localhost:8083/api/languages";
		
		ResponseEntity<Languages[]> response = restTemplate.getForEntity(url, Languages[].class);
		Languages[] languages = response.getBody();

		return Arrays.asList(languages);
	}

	public List<Languages> getAllSeekerLanguage(int id) {
		
		String url = "http://localhost:8083/jobseeker/languages/" + id;
		
		ResponseEntity<Languages[]> response = restTemplate.getForEntity(url, Languages[].class);
		Languages[] languages = response.getBody();

		return Arrays.asList(languages);
		
	}

	public String addLanguages(int id, List<Integer> languageIds, RedirectAttributes redirectAttributes) {
		
		String url = "http://localhost:8083/jobseeker/language/add/" + id;

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
			for (Integer languageId : languageIds) {
				body.add("languageIds", languageId.toString());
			}

			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

			restTemplate.postForEntity(url, request, String.class);

			redirectAttributes.addFlashAttribute("success", "Languages added successfully.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Failed to add language: " + e.getMessage());
		}

		return "redirect:/profile";
	}

	public String deleteSeekerLanguage(int id, int jobseekerId , RedirectAttributes redirectAttributes) {
		String url = "http://localhost:8083/jobseeker/language/delete/" + id + "/" +  jobseekerId;

		try {
			restTemplate.delete(url);
			redirectAttributes.addFlashAttribute("success", "language deleted successfully!");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			redirectAttributes.addFlashAttribute("error", "Error occured while deleting language!");
		}

		return "redirect:/profile";
	}

	public List<Projects> getAllProjects(int id) {

		String url = "http://localhost:8083/jobseeker/projects/" + id;

		ResponseEntity<Projects[]> response = restTemplate.getForEntity(url, Projects[].class);

		return Arrays.asList(response.getBody());
	}

	public String addProject(int id, Projects project, RedirectAttributes redirectAttributes) {
		String url = "http://localhost:8083/jobseeker/project/add/" + id;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Projects> requestEntity = new HttpEntity<>(project, headers);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			redirectAttributes.addFlashAttribute("success", "project added successfully!");
		} else {
			redirectAttributes.addFlashAttribute("error", "error adding project!");
		}
		return "redirect:/profile";
	}

	public String editProject(Projects project, RedirectAttributes redirectAttributes) {
		String url = "http://localhost:8083/jobseeker/project/update/" + project.getId();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Projects> requestEntity = new HttpEntity<>(project, headers);

		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);

			if (response.getStatusCode().is2xxSuccessful()) {
				redirectAttributes.addFlashAttribute("success", "Project updated successfully!");
			} else {
				redirectAttributes.addFlashAttribute("error", "Failed to update Project.");
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error while updating project" + e.getMessage());
		}
		return "redirect:/profile";
	}
	
	public String deleteProject(int id, RedirectAttributes redirectAttributes) {
		String url = "http://localhost:8083/jobseeker/project/delete/" + id;

		try {
			restTemplate.delete(url);
			redirectAttributes.addFlashAttribute("success", "project deleted successfully!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error occured while deleting project!");
		}

		return "redirect:/profile";
	}

	public List<Experience> getAllExperience(int id) {
		String url = "http://localhost:8083/jobseeker/experience/" + id;

		ResponseEntity<Experience[]> response = restTemplate.getForEntity(url, Experience[].class);

		return Arrays.asList(response.getBody());
	}

	public String addExperience(int id, Experience experience, RedirectAttributes redirectAttributes) {
		String url = "http://localhost:8083/jobseeker/experience/add/" + id;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Experience> requestEntity = new HttpEntity<>(experience, headers);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			redirectAttributes.addFlashAttribute("success", "Experience added successfully!");
		} else {
			redirectAttributes.addFlashAttribute("error", "error adding experience!");
		}
		return "redirect:/profile";
	}

	public String editExperience(Experience experience, RedirectAttributes redirectAttributes) {
		String url = "http://localhost:8083/jobseeker/experience/update/" + experience.getId();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Experience> requestEntity = new HttpEntity<>(experience, headers);

		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);

			if (response.getStatusCode().is2xxSuccessful()) {
				redirectAttributes.addFlashAttribute("success", "Experience updated successfully!");
			} else {
				redirectAttributes.addFlashAttribute("error", "Failed to update experience.");
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error while updating experience" + e.getMessage());
		}
		return "redirect:/profile";
	}

	public String deleteExperience(int id, RedirectAttributes redirectAttributes) {
		String url = "http://localhost:8083/jobseeker/experience/delete/" + id;

		try {
			restTemplate.delete(url);
			redirectAttributes.addFlashAttribute("success", "experience deleted successfully!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error occured while deleting experience!");
		}

		return "redirect:/profile";
	}
}
