package com.talenttap.DTO;

import com.talenttap.entity.Location;

public class LocationDTO {

	private int locationId;
	private String location;
	
	public LocationDTO(Location location) {
		
		this.locationId = location.getLocationId();
		this.location = location.getLocation();
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
