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
	
	// this method is unnecessary since count gets increased/decreased only by
	// adding/removing methods 
	//public void setFacilityCount(int count) {
	//	facilityCount = count;
	//}
	
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
