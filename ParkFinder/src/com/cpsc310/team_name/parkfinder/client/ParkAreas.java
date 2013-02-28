package com.cpsc310.team_name.parkfinder.client;

import java.io.Serializable;
import java.util.ArrayList;

public class ParkAreas implements Serializable {
	
	private int parkId;
	private int areaCount;
	private ArrayList<Area> areas;
	
	public ParkAreas(){
		// default constructor
	}
	
	public ParkAreas(int theParkId) {
		parkId = theParkId;
	}
	
	public int getParkId() {
		return parkId;
	}
	
	public int getAreaCount() {
		return areaCount;
	}
	
	public ArrayList<Area> getAreas() {
		return areas;
	}
	
	// this method is unnecessary because the parkId is set with the constructor
//	public void setParkId(int theId) {
//		parkId = theId;
//	}
	
	public void setAreaCount(int count) {
		areaCount = count;
	}
	
	public void addArea(Area anArea) {
		areas.add(anArea);
	}
	
	public void removeArea(Area anArea) {
		// TODO
	}
	
	public void containsArea(Area anArea) {
		// TODO
	}
	
	

}
