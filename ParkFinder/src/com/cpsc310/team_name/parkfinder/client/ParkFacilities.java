package com.cpsc310.team_name.parkfinder.client;

import java.io.Serializable;
import java.util.ArrayList;

public class ParkFacilities implements Serializable {
	

	private String parkId;
	private int facilityCount;
	private ArrayList<Facility> facilities;
	

	public ParkFacilities(String id, ArrayList<Facility> theFacilities) {
		parkId = id;
		facilities = theFacilities;	
	}
	

	public String getParkId() {
		return parkId;
	}
	

	public ArrayList<Facility> getFacilities() {
		return facilities;
	}
	
	public void addFacility(Facility theFacility) {
		facilities.add(theFacility);
		facilityCount++;
	}
	
	public void removeFacility(Facility theFacility) {
		for(int i=0;i<facilities.size();i++) {
			if(facilities.get(i).getFacilityType().equals(theFacility.getFacilityType())) {
				facilities.remove(i);
				facilityCount--;
			}
		}
	}
	
	public boolean containsFacility(Facility theFacility) {
		for(Facility f:facilities) {
			if(f.getFacilityType().equals(theFacility.getFacilityType())) {
				return true;
			}
		}
		return false;
	}
	

}
