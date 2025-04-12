package com.talenttap.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.talenttap.DTO.EducationLevelDTO;
import com.talenttap.repository.EducationLevelRepository;

@Service
public class EducationService {

	private EducationLevelRepository educationLevelRepo;
	
	public EducationService(EducationLevelRepository educationLevelRepo) {
		this.educationLevelRepo = educationLevelRepo;
	}

	public List<EducationLevelDTO> getAllEducationLevel() {
		List<EducationLevelDTO> educationLevels = educationLevelRepo.findAll().stream().map(EducationLevelDTO::new).toList();
		return educationLevels;
	}
	
	
}
