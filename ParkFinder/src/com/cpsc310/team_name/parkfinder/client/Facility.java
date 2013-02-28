package com.cpsc310.team_name.parkfinder.client;

import java.io.Serializable;

public class Facility implements Serializable{
	
	private String facilityType;
	
	public Facility(){
		// default constructor
	}

	public Facility(String afacilityType) {
		facilityType = afacilityType;
	}
	
	public String getFacilityType() {
		return facilityType;
	}
	
}
