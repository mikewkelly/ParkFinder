package com.cpsc310.team_name.parkfinder.client;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Facility implements Serializable{
	
	private String parkId;
	
	private String facilityType;
	
	private int facilityCount;
	
	public Facility(){
		// default constructor
	}


	public Facility(String aParkId, String aFacilityType, int aFacilityCount) {
		parkId = aParkId;
		facilityType = aFacilityType;
		facilityCount = aFacilityCount;
	}
	
	public String getParkId() {
		return parkId;
	}
	
	public String getFacilityType() {
		return facilityType;
	}
	
	public int getFacilityCount() {
		return facilityCount;
	}

}
