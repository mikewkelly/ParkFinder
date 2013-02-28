package com.cpsc310.team_name.parkfinder.client;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Facility implements Serializable{
	
	private String facilityType;
	private int facilityCount;

	public Facility(String aFacilityType, int aFacilityCount) {
		facilityType = aFacilityType;
		facilityCount = aFacilityCount;
	}
	
	public String getFacilityType() {
		return facilityType;
	}
	
	public int getFacilityCount() {
		return facilityCount;
	}

}