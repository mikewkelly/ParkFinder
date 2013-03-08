package com.cpsc310.team_name.parkfinder.client;

import java.io.Serializable;
import java.util.ArrayList;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

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
	private ArrayList<Facility> parkFacilities;
	
	@Persistent
	private ArrayList<Area> parkAreas;

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
	
	// These methods deal with the Facilities
	
	public ArrayList<Facility> getParkFacilities() {
		return parkFacilities;
	}
	
	public void setParkFacilities(ArrayList<Facility> theFacilities) {
		parkFacilities = theFacilities;
	}
	
	public void addFacility(Facility theFacility) {
		if (!this.containsFacilityByType(theFacility.getFacilityType())) {
			parkFacilities.add(theFacility);
		}
	}
	
	public Facility getFacilityByType(String theFacilityType) {
		for (Facility f: parkFacilities) {
			if (f.getFacilityType().equals(theFacilityType)) {
				return f;
			}
		}
		return new Facility("","",0, ""); // can't return null in GWT, so return "blank" Facility
	}
	
	
	public boolean containsFacility(Facility theFacility) {
		for(Facility f:parkFacilities) {
			if( (f.getParkId().equals(theFacility.getParkId())) && (f.getFacilityType().equals(theFacility.getFacilityType()))) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsFacilityByType(String theFacilityType) {
		for (Facility f:parkFacilities) {
			if (f.getFacilityType().toUpperCase().equals(theFacilityType.toUpperCase())) {
				return true;
			}
		}
		return false;
	}
	
	// These methods deal with the Areas
	
	public ArrayList<Area> getParkAreas() {
		return parkAreas;
	}
	
	public void setParkAreas(ArrayList<Area> theAreas) {
		parkAreas = theAreas;
	}
	
	public void addArea(Area anArea) {	
		parkAreas.add(anArea);
	}
	
	public boolean containsArea(Area anArea) {
		for (Area a: parkAreas) {
			if (a.getSiteArea().toUpperCase().equals(anArea.getSiteArea().toUpperCase())) {
				return true;
			}
		}
		return false;
	}
	

}
