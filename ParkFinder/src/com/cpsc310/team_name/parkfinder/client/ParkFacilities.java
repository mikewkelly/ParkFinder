package com.cpsc310.team_name.parkfinder.client;

import java.io.Serializable;
import java.util.ArrayList;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ParkFacilities implements Serializable {
	
	@Persistent
	private String parkId;
	
	@Persistent
	private int facilityCount;
	
	@Persistent
	private ArrayList<String> facilities;
	

	public ParkFacilities(){
		// default constructor
	}
	
	
	public ParkFacilities(String id, ArrayList<String> theFacilities) {

		parkId = id;
		facilities = theFacilities;	
		//facilityCount = facilities.size();
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
		return facilities.size();
	}
	


	public String getParkId() {
		return parkId;
	}
	

	public ArrayList<String> getFacilities() {
		return facilities;
	}
	
	public void addFacility(String theFacility) {
		facilities.add(theFacility);
		facilityCount++;
	}
	
	public void removeFacility(String theFacility) {

		for(int i=0;i<facilities.size();i++) {
			if(facilities.get(i).equals(theFacility)) {
				facilities.remove(i);
				facilityCount--;
			}
		}
	}
	
	public boolean containsFacility(String theFacility) {

		for(String f:facilities) {
			if(f.equals(theFacility)) {
				return true;
			}
		}
		return false;
	}
	

}
