package com.cpsc310.team_name.parkfinder.client;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LatLong implements Serializable {
	
	private float longitude;
	private float latitude;
	

	public LatLong() {
		
	}
	
	public float getLat() {
		return latitude;
	}
	
	public float getLong() {
		return longitude;
	}
	public void setLat(float lat){
		latitude=lat;
	}
	public void setLong(float lon){
		longitude = lon;
	}
}


