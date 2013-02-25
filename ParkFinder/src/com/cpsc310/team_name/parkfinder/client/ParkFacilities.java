package com.cpsc310.team_name.parkfinder.client;

import java.io.Serializable;
import java.util.ArrayList;

public class ParkFacilities implements Serializable {
	
	private int parkId;
	private ArrayList<Facility> facilities;
	
	public ParkFacilities(int id, ArrayList<Facility> theFacilities) {
		parkId = id;
		facilities = theFacilities;
	}
	
	public void setParkId(int anId) {
		parkId = anId;
	}
	
	
	public int getParkId() {
		return parkId;
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
