package com.cpsc310.team_name.parkfinder.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("Park")
public interface ParkService extends RemoteService {
	void addPark(String parkId);
	
	Park[] getParks();
	
	void removePark(String parkId);

}
