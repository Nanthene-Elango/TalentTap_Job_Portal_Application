package com.talenttap.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talenttap.DTO.IndustryTypeDTO;
import com.talenttap.service.IndustryTypeService;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class EmployerController {
	private IndustryTypeService industryTypeService;
	
	public EmployerController( IndustryTypeService industryTypeService) {
		this.industryTypeService = industryTypeService;	
	}
	
	@GetMapping("/industry")
	public ResponseEntity<List<IndustryTypeDTO>> getAllIndustryType(){
		return ResponseEntity.ok().body(industryTypeService.getAllIndustryType());
		}
	

}
