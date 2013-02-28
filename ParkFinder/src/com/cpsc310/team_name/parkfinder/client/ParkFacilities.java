package com.cpsc310.team_name.parkfinder.client;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class ParkFacilities implements Serializable {
	

	private String parkId;
	private int facilityCount;
	private ArrayList<Facility> facilities;
	

	public ParkFacilities(){
		// default constructor
	}
	

	public ParkFacilities(String id, ArrayList<Facility> theFacilities) {

		parkId = id;
		facilities = theFacilities;	
		facilityCount = facilities.size();
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
	

	
	// I think for this we just return the size of the list
	// so we don't need the extra attribute?
	public int getFacilityCount() {
		return facilityCount;
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
