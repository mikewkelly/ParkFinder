package com.cpsc310.team_name.parkfinder.client;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LatLong implements Serializable {
	
	private double longitude;
	private double latitude;
	

	public LatLong() {
	}
	
	public LatLong(Double lat, Double lon) {
		this.latitude = lat;
		this.longitude = lon;

	}
	
	public double getLat() {
		return latitude;
	}
	
	public double getLong() {
		return longitude;
	}
	
}


