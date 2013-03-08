package com.cpsc310.team_name.parkfinder.client;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Area implements Serializable {
	
	private String areaId; // a value unique to each Area object
	private String parkId;
	private String parkName;
	private String siteArea;
	private String closureNotes;
	private String weekendStatus;
	private String lastUpdated;
	
	public Area() {
		
	}
	
	public Area(String theParkId, String anArea, String anAreaId) {
		parkId = theParkId;
		siteArea = anArea;
		areaId = anAreaId;
	}
	
	public String getParkId() {
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
