package com.cpsc310.team_name.parkfinder.client;

import java.io.Serializable;
import java.util.ArrayList;

public class ParkFacilities implements Serializable {
	
	private int parkId;
	private int facilityCount;
	private ArrayList<Facility> facilities;
	
	public ParkFacilities(){
		// default constructor
	}
	
	public ParkFacilities(int id) {
		parkId = id;
	}
	
	// I think this method is unnecessary since id is set when it is created
	// using the constructor
	//public void setParkId(int anId) {
	//	parkId = anId;
	//}
	
	// The facilityCount gets increased/decreased when adding/removing
	// facility so no need to set it manually
	//public void setFacilityCount(int count) {
	//	facilityCount = count;
	//}
	
	public int getParkId() {
		return parkId;
	}
	
	// I think for this we just return the size of the list
	// so we don't need the extra attribute?
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
		// remove the facility if the type matches
		// may remove multiple occurrences
		for(int i=0;i<facilities.size();i++) {
			if(facilities.get(i).getFacilityType().equals(theFacility.getFacilityType())) {
				facilities.remove(i);
				facilityCount--;
			}
		}
	}
	
	public boolean containsFacility(Facility theFacility) {
		// return true if theFacility is in the list
		for(Facility f:facilities) {
			if(f.getFacilityType().equals(theFacility.getFacilityType())) {
				return true;
			}
		}
		return false;
	}
	

}
