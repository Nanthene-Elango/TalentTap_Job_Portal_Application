package com.talenttap.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.talenttap.DTO.LocationDTO;
import com.talenttap.entity.Location;
import com.talenttap.repository.LocationRepository;

@Service
public class LocationService {

	private LocationRepository locationRepository;
	
	public LocationService(LocationRepository locationRepository) {
		this.locationRepository = locationRepository;
	}
	
	public List<LocationDTO> getAllLocations(){
		List<LocationDTO> locations = locationRepository.findAll().stream().map(LocationDTO::new).toList();
		return locations;
	}
	
}
