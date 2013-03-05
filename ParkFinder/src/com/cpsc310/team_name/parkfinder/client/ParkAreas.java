package com.cpsc310.team_name.parkfinder.client;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class ParkAreas implements Serializable {

	private String parkId;
	private int areaCount;
	private ArrayList<Area> areas;
	
	public ParkAreas(){
		// default constructor
	}
	
	public ParkAreas(String theParkId) {

		parkId = theParkId;
	}
	
	public String getParkId() {
		return parkId;
	}
	
	public int getAreaCount() {
		return areaCount;
	}
	
	public ArrayList<Area> getAreas() {
		return areas;
	}
	
	public void setAreaCount(int count) {
		areaCount = count;
	}
	
	public void addArea(Area anArea) {
		areas.add(anArea);
	}
	
	public void removeArea(Area anArea) {
		for (int i = 0; i < areas.size(); i++ ) {
			if ((areas.get(i).getParkId().equals(anArea.getParkId())) && (areas.get(i).getSiteArea().equals(anArea.getSiteArea()))) {
				areas.remove(i);
			}
		}
	}
	

	public boolean containsArea(Area anArea) {
		for (Area a: areas) {
			if ((a.getParkId().equals(anArea.getParkId())) && (a.getSiteArea().equals(anArea.getSiteArea()))) {
				return true;
			}
		}
		return false;
	}

}
