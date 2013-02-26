package com.cpsc310.team_name.parkfinder.client;

import java.io.Serializable;

public class LatLong implements Serializable {
	
	private float longitude;
	private float latitude;
	
	public LatLong() {
		
	}
	
	public LatLong(float lat, float lon) {
		this.latitude = lat;
		this.longitude = lon;

	}
	
	public float getLat() {
		return latitude;
	}
	
	public float getLong() {
		return longitude;
	}
	
	public void setLat(float theLat) {
		latitude = theLat;
	}
	
	public void setLong(float theLong) {
		longitude = theLong;
	}
	
	

}
