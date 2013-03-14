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
	
	public Facility(){
		// default constructor
	}


	public Facility(Long aParkId, String aFacilityType, String aFacilityId) {
		parkId = aParkId;
		facility = aFacilityType;
		facilityId = aFacilityId;
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
	
	public void setParkId(Long id)
	{
		parkId = id;
	}
	public void setFacility(String facilityToAdd)
	{
		facility = facilityToAdd;
	}
}
