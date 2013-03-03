package com.cpsc310.team_name.parkfinder.client;

import java.io.Serializable;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Park implements Serializable{
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String parkId;
	
	@Persistent
	private String name;
	
	@Persistent
	private int streetNumber;
	
	@Persistent
	private String streetName;
	
	@Persistent(serialized = "true")
	private LatLong googleMapDest;
	
	@Persistent
	private String neighbourhoodName;
	
	@Persistent(serialized = "true")
	private ParkFacilities parkFacilities;
	
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
	
	public int getStreetNumber() {
		return streetNumber;
	}
	
	public String getStreetName() {
		return streetName;
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
	
	public void setStreetName(String theName) {
		streetName = theName;
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
