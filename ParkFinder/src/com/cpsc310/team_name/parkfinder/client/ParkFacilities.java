package com.cpsc310.team_name.parkfinder.client;

import java.io.Serializable;
import java.util.ArrayList;

public class ParkFacilities implements Serializable {
	
	private int parkId;
	private int facilityCount;
	private ArrayList<Facility> facilities;
	
	public ParkFacilities(int id) {
		parkId = id;
	}
	
	public void setParkId(int anId) {
		parkId = anId;
	}
	
	public void setFacilityCount(int count) {
		facilityCount = count;
	}
	
	public int getParkId() {
		return parkId;
	}
	
	public int getFacilityCount() {
		return facilityCount;
	}
	
	public ArrayList<Facility> getFacilities() {
		return facilities;
	}
	
	public void addFacility(Facility theFacility) {
		facilities.add(theFacility);
	}
	
	public void removeFacility(Facility theFacility) {
		// TODO
	}
	
	public void containsFacility(Facility theFacility) {
		// TODO
	}
	

}
