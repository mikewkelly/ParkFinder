package com.cpsc310.team_name.parkfinder.client;

import java.io.Serializable;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Area implements Serializable {
	
	@PrimaryKey
	private String areaId; // a value unique to each Area object
	@Persistent
	private Long parkId;
	@Persistent
	private String parkName;
	@Persistent
	private String siteArea;
	@Persistent
	private String closureNotes;
	@Persistent
	private String weekendStatus;
	@Persistent
	private String lastUpdated;
	
	

	public Area() {
		
	}
	
	public Area(Long theParkId, String theAreaId,String anArea) {
		parkId = theParkId;
		siteArea = anArea;
		areaId = theAreaId;
	}
	public String getAreaId(){
		return areaId;
	}
	
	
	public Long getParkId() {
		return parkId;
	}
	
	public String getParkName() {
		return parkName;
	}
	
	public String getSiteArea() {
		return siteArea;
	}
	
	public String getClosureNotes() {
		return closureNotes;
	}
	
	public String getWeekendStatus() {
		return weekendStatus;
	}
	
	public String getLastUpdated() {
		return lastUpdated;
	}
	
	public void setParkName(String theName) {
		parkName = theName;
	}
	
	public void setClosureNotes(String theNotes) {
		closureNotes = theNotes;
	}
	
	public void setWeekendStatus(String theStatus) {
		weekendStatus = theStatus;
	}
	
	public void setLastUpdated(String newLastUpdated) {
		lastUpdated = newLastUpdated;
	}
	

}
