package com.talenttap.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.talenttap.DTO.SkillsDTO;
import com.talenttap.repository.SkillsRepository;

@Service
public class SkillService {

    private SkillsRepository skillsRepository;
	
	public SkillService(SkillsRepository skillsRepository) {
		this.skillsRepository = skillsRepository;
	}
	
	public List<SkillsDTO> getAllSkills(){
		List<SkillsDTO> locations = skillsRepository.findAll().stream().map(SkillsDTO::new).toList();
		return locations;
	}
}
