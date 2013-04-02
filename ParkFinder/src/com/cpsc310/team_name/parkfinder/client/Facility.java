package com.cpsc310.team_name.parkfinder.client;

import java.io.Serializable;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Facility implements Serializable{
	
	@PrimaryKey
	private String facilityId;
	@Persistent
	private Long parkId;
	@Persistent
	private String facility;
	@Persistent
	private String facilityCount;
	public Facility(){
		// default constructor
	}

	public Facility(Long aParkId, String aFacilityType, String aFacilityId, String aFacilityCount) {
		parkId = aParkId;
		facility = aFacilityType;
		facilityId = aFacilityId;
		facilityCount = aFacilityCount;
	}

	public Long getParkId() {
		return parkId;
	}
	
	public String getFacility() {
		return facility;
	}
	public String getFacilityID(){
		return facilityId;
	}
	public String getFacilityCount(){
		return facilityCount;
	}
	public void setParkId(Long id)
	{
		parkId = id;
	}
	public void setFacility(String facilityToAdd)
	{
		facility = facilityToAdd;
	}
	
}
