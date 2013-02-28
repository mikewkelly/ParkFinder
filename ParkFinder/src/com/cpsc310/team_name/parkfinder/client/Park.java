package com.cpsc310.team_name.parkfinder.client;

import java.io.Serializable;

public class Park implements Serializable{
	
	private int parkId;
	private String name;
	private int streetNumber;
	private LatLong googleMapDest;
	private String neighbourhoodName;
	private ParkFacilities parkFacilities;
	private ParkAreas parkAreas;
	
	public Park() {
		// default constructor
	}
	
	public Park(int theParkId) {
		parkId = theParkId;
	}
	
	public int getParkId() {
		return parkId;
	}
	
	public String getName() {
		return name;
	}
	
	public int getStreetNumber() {
		return streetNumber;
	}
	
	public LatLong getGoogleMapDest() {
		return googleMapDest;
	}
	
	public String getNeighbourhoodName() {
		return neighbourhoodName;
	}
	
	public ParkFacilities getParkFacilities() {
		return parkFacilities;
	}
	
	public ParkAreas getParkAreas() {
		return parkAreas;
	}
	
	// this method is unnecessary because the parkId is used in the constructor
//	public void setParkId(int theParkId) {
//		parkId = theParkId;
//	}
//	
	
	public void setName(String theName) {
		name = theName;
	}
	
	public void setStreetNumber(int theNumber) {
		streetNumber = theNumber;
	}
	
	public void setGoogleMapDest(LatLong theGMD) {
		googleMapDest = theGMD;
	}
	
	public void setNeighbourhoodName(String theName) {
		neighbourhoodName = theName;
	}
	
	public void setParkFacilities(ParkFacilities theFacilities) {
		parkFacilities = theFacilities;
	}
	
	public void setParkAreas(ParkAreas theAreas){
		parkAreas = theAreas;
	}


}
