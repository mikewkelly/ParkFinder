package com.cpsc310.team_name.parkfinder.client;

import java.io.Serializable;

public class Area implements Serializable {
	
	private String parkId;
	private String siteArea;
	private String closureNotes;
	private String weekendStatus;
	private String lastUpdated;
	
	public Area(String theParkId) {
		parkId = theParkId;
	}
	
	public String getParkId() {
		return parkId;
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
	
	public void setSiteArea(String anArea) {
		siteArea = anArea;
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
