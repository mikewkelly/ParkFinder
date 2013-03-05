package com.cpsc310.team_name.parkfinder.client;

import java.io.Serializable;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.ArrayList;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Park implements Serializable{
	
	@PrimaryKey
	private String parkId;
	
	@Persistent
	private String name;
	
	@Persistent
	private String streetNumber;
	
	@Persistent
	private String streetName;
	
	//@Persistent(serialized = "true")
	//private LatLong googleMapDest;
	@Persistent
	private String googleMapDest;
	
	@Persistent
	private String neighbourhoodName;
	
	@Persistent
	private String parkFacilities;
	
	@Persistent(serialized = "true")
	private ParkAreas parkAreas;
	

	public Park() {
		// default constructor
	}
	
	public Park(String theParkId) {

		parkId = theParkId;
	}
	
	public String getParkId() {
		return parkId;
	}
	
	public String getName() {
		return name;
	}
	
	public String getStreetNumber() {
		return streetNumber;
	}
	
	public String getStreetName() {
		return streetName;
	}
	
	public String getGoogleMapDest() {
		return googleMapDest;
	}
	
	public String getNeighbourhoodName() {
		return neighbourhoodName;
	}
	
	public String getParkFacilities() {
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
	
	public void setStreetNumber(String theNumber) {
		streetNumber = theNumber;
	}
	
	public void setStreetName(String theName) {
		streetName = theName;
	}
	
	public void setGoogleMapDest(String theGMD) {
		googleMapDest = theGMD;
	}
	
	public void setNeighbourhoodName(String theName) {
		neighbourhoodName = theName;
	}
	
	public void setParkFacilities(String theFacilities) {
		parkFacilities = theFacilities;
	}
	
	public void setParkAreas(ParkAreas theAreas){
		parkAreas = theAreas;
	}

}
