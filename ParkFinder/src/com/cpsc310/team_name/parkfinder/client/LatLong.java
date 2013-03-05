package com.cpsc310.team_name.parkfinder.client;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LatLong implements Serializable {
	
	private float longitude;
	private float latitude;
	

	public LatLong() {
		super();

	}
	
	public float getLat() {
		return this.latitude;
	}
	
	public float getLong() {
		return this.longitude;
	}

	public void setLat(float lat){
		this.latitude=lat;
	}
	public void setLong(float lon){
		this.longitude = lon;
	}
}


