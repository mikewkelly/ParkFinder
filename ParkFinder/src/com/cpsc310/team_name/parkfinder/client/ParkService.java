package com.cpsc310.team_name.parkfinder.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("Park")
public interface ParkService extends RemoteService {
	void addPark(int parkId);
	
	Park[] getParks();
	
	void removePark(int parkId);

}
