package com.talenttap.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.talenttap.DTO.IndustryTypeDTO;
import com.talenttap.repository.IndustryTypeRepository;

@Service
public class IndustryTypeService {
	
	private IndustryTypeRepository industryTypeRepository;
	
	public IndustryTypeService(IndustryTypeRepository industryTypeRepository) {
		this.industryTypeRepository = industryTypeRepository;
	}
	
	public List<IndustryTypeDTO> getAllIndustryType(){
		List<IndustryTypeDTO> industryType = industryTypeRepository.findAll().stream().map(IndustryTypeDTO::new).toList();
		return industryType;
	}
	

}
